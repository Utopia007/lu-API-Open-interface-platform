package com.luyouxiao.project;

import com.luyouxiao.project.utils.SignUtils;
import com.luyouxiao.yuapicommon.model.entity.InterfaceInfo;
import com.luyouxiao.yuapicommon.model.entity.User;
import com.luyouxiao.yuapicommon.service.InnerInterfaceInfoService;
import com.luyouxiao.yuapicommon.service.InnerUserInterfaceInfoService;
import com.luyouxiao.yuapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @auther : LuYouxiao
 * @date 2024/3/9   -17:43
 * @Description
 */

@Slf4j
@Component
public class CustomGlobleFilter implements GlobalFilter, Ordered {

    private static final String WHITE_FORM = "127.0.0.1,localhost";

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final String INTERFACE_HOST = "http://localhost:8080";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //1、用户发送请求到API网关
        //2、请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().toString();
        String method = Objects.requireNonNull(request.getMethod()).toString();
        log.info("请求日志：cookies-------" + request.getCookies());
        log.info("请求日志：ID-------" + request.getId());
        log.info("请求日志：路径-------" + path);
        log.info("请求日志：方法-------" + method);
        log.info("请求日志：请求参数-------" + request.getQueryParams());
        log.info("请求日志：remote地址-------" + request.getRemoteAddress());
        String localAddress = String.valueOf(request.getLocalAddress().getHostString());
        log.info("请求日志：local地址-------" + localAddress);
        // todo 写入到日志文件

        ServerHttpResponse response = exchange.getResponse();
        //3、黑白名单
        if (!WHITE_FORM.contains(localAddress)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        //4、用户鉴权（判断ak、sk是否合法）
        // 从请求头中获取参数
        String accessKey = request.getHeaders().getFirst("accessKey");
        String nonce = request.getHeaders().getFirst("nonce");
        String timestamp = request.getHeaders().getFirst("timestamp");
        String sign = request.getHeaders().getFirst("sign");
        String body = request.getHeaders().getFirst("body");
        // todo 实际情况应该是去数据库中查是否已分配给用户
        User invokerUser = null;
        try {
            invokerUser = innerUserService.getInvoker(accessKey);
        } catch (Exception e) {
            log.info("getInvoker error ", e);
        }
        if (invokerUser == null) {
            return handleNoAuth(response);
        }
//        if (!accessKey.equals("admin")) {
//            return handleNoAuth(response);
//        }
        // 直接校验如果随机数大于1万，则抛出异常，并提示"无权限"
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        // 时间和当前时间不能超过5分钟
//        Long currentTime = System.currentTimeMillis() / 1000;
//        final Long FIVE_MINUTE = 60 * 5L;
//        if (currentTime - Long.parseLong(timestamp) > FIVE_MINUTE) {
//            return handleNoAuth(response);
//        }
        // todo 实际情况中是从数据库中查出 secretKey
        String secretKey = invokerUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        // 如果生成的签名不一致，则抛出异常，并提示"无权限"
        if (sign == null || !sign.equals(serverSign)) {
            throw new RuntimeException("无权限");
        }
        //5、todo 在数据库中判断请求的接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.info("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        //6、请求转发、调用模拟接口
//        Mono<Void> filter = chain.filter(exchange);
        //7、响应日志
        return handleResponse(exchange, chain, invokerUser.getId(), interfaceInfo.getId());

//
//
//        return chain.filter(exchange);
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long invokerUserid, long interfaceInfoId) {
        try {
            // 获取原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            // 判断状态码是否为200 OK(按道理来说,现在没有调用,是拿不到响应码的,对这个保持怀疑 沉思.jpg)
            if (statusCode == HttpStatus.OK) {
                // 创建一个装饰后的响应对象(开始穿装备，增强能力)
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    // 重写writeWith方法，用于处理响应体的数据
                    // 这段方法就是只要当我们的模拟接口调用完成之后,等它返回结果，
                    // 就会调用writeWith方法,我们就能根据响应结果做一些自己的处理
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        // 判断响应体是否是Flux类型
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 返回一个处理后的响应体
                            // (这里就理解为它在拼接字符串,它把缓冲区的数据取出来，一点一点拼接好)
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                    //todo 调用成功，接口调用次数+1
                                try {
                                    innerUserInterfaceInfoService.invokeCount(invokerUserid, interfaceInfoId);
                                } catch (Exception e) {
                                    log.info("invokeCount error", e);
                                }
                                // 读取响应体的内容并转换为字节数组
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    DataBufferUtils.release(dataBuffer);//释放掉内存
                                    // 构建日志
                                    StringBuilder sb2 = new StringBuilder(200);
                                    List<Object> rspArgs = new ArrayList<>();
                                    rspArgs.add(originalResponse.getStatusCode());
                                    //rspArgs.add(requestUrl);
                                    String data = new String(content, StandardCharsets.UTF_8);//data

                                    sb2.append(data);
                                    log.info("响应结果：" + data);
                                    // 将处理后的内容重新包装成DataBuffer并返回
                                    return bufferFactory.wrap(content);
                                }));
                        } else {
                            //todo 9、调用失败，返回错误码

                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 对于200 OK的请求,将装饰后的响应对象传递给下一个过滤器链,并继续处理(设置repsonse对象为装饰过的)
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 对于非200 OK的请求，直接返回，进行降级处理
            return chain.filter(exchange);
        } catch (Exception e) {
            // 处理异常情况，记录错误日志
            log.error("网管处理响应异常\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
