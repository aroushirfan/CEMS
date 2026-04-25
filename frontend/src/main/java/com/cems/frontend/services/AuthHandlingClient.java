package com.cems.frontend.services;

import com.cems.frontend.controller.components.SidebarController;
import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.HttpStatus;
import com.cems.frontend.utils.LocalStorage;
import com.cems.frontend.view.SceneNavigator;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URLPermission;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AuthHandlingClient extends HttpClient {

    public class UnauthorizedException extends Exception {
    }

    private final HttpClient client;

    public AuthHandlingClient(HttpClient client) {
        this.client = client;
    }

    /**
     * Returns an {@code Optional} containing this client's {@link
     * CookieHandler}. If no {@code CookieHandler} was set in this client's
     * builder, then the {@code Optional} is empty.
     *
     * @return an {@code Optional} containing this client's {@code CookieHandler}
     */
    @Override
    public Optional<CookieHandler> cookieHandler() {
        return client.cookieHandler();
    }

    /**
     * Returns an {@code Optional} containing the <i>connect timeout duration</i>
     * for this client. If the {@linkplain Builder#connectTimeout(Duration)
     * connect timeout duration} was not set in the client's builder, then the
     * {@code Optional} is empty.
     *
     * @return an {@code Optional} containing this client's connect timeout
     * duration
     */
    @Override
    public Optional<Duration> connectTimeout() {
        return client.connectTimeout();
    }

    /**
     * Returns the follow redirects policy for this client. The default value
     * for client's built by builders that do not specify a redirect policy is
     * {@link Redirect#NEVER NEVER}.
     *
     * @return this client's follow redirects setting
     */
    @Override
    public Redirect followRedirects() {
        return client.followRedirects();
    }

    /**
     * Returns an {@code Optional} containing the {@code ProxySelector}
     * supplied to this client. If no proxy selector was set in this client's
     * builder, then the {@code Optional} is empty.
     *
     * <p> Even though this method may return an empty optional, the {@code
     * HttpClient} may still have a non-exposed {@linkplain
     * Builder#proxy(ProxySelector) default proxy selector} that is
     * used for sending HTTP requests.
     *
     * @return an {@code Optional} containing the proxy selector supplied
     * to this client.
     */
    @Override
    public Optional<ProxySelector> proxy() {
        return client.proxy();
    }

    /**
     * Returns this client's {@code SSLContext}.
     *
     * <p> If no {@code SSLContext} was set in this client's builder, then the
     * {@linkplain SSLContext#getDefault() default context} is returned.
     *
     * @return this client's SSLContext
     */
    @Override
    public SSLContext sslContext() {
        return client.sslContext();
    }

    /**
     * Returns a copy of this client's {@link SSLParameters}.
     *
     * <p> If no {@code SSLParameters} were set in the client's builder, then an
     * implementation specific default set of parameters, that the client will
     * use, is returned.
     *
     * @return this client's {@code SSLParameters}
     */
    @Override
    public SSLParameters sslParameters() {
        return client.sslParameters();
    }

    /**
     * Returns an {@code Optional} containing the {@link Authenticator} set on
     * this client. If no {@code Authenticator} was set in the client's builder,
     * then the {@code Optional} is empty.
     *
     * @return an {@code Optional} containing this client's {@code Authenticator}
     */
    @Override
    public Optional<Authenticator> authenticator() {
        return client.authenticator();
    }

    /**
     * Returns the preferred HTTP protocol version for this client. The default
     * value is {@link Version#HTTP_2}
     *
     * @return the HTTP protocol version requested
     * @implNote Constraints may also affect the selection of protocol version.
     * For example, if HTTP/2 is requested through a proxy, and if the
     * implementation does not support this mode, then HTTP/1.1 may be used
     */
    @Override
    public Version version() {
        return client.version();
    }

    /**
     * Returns an {@code Optional} containing this client's {@link
     * Executor}. If no {@code Executor} was set in the client's builder,
     * then the {@code Optional} is empty.
     *
     * <p> Even though this method may return an empty optional, the {@code
     * HttpClient} may still have an non-exposed {@linkplain
     * Builder#executor(Executor) default executor} that is used for
     * executing asynchronous and dependent tasks.
     *
     * @return an {@code Optional} containing this client's {@code Executor}
     */
    @Override
    public Optional<Executor> executor() {
        return client.executor();
    }

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link HttpResponse}{@code <T>} contains the
     * response status, headers, and body ( as handled by given response body
     * handler ).
     *
     * <p> If the operation is interrupted, the default {@code HttpClient}
     * implementation attempts to cancel the HTTP exchange and
     * {@link InterruptedException} is thrown.
     * No guarantee is made as to exactly <em>when</em> the cancellation request
     * may be taken into account. In particular, the request might still get sent
     * to the server, as its processing might already have started asynchronously
     * in another thread, and the underlying resources may only be released
     * asynchronously.
     * <ul>
     *     <li>With HTTP/1.1, an attempt to cancel may cause the underlying
     *         connection to be closed abruptly.
     *     <li>With HTTP/2, an attempt to cancel may cause the stream to be reset,
     *         or in certain circumstances, may also cause the connection to be
     *         closed abruptly, if, for instance, the thread is currently trying
     *         to write to the underlying socket.
     * </ul>
     *
     * @param request             the request
     * @param responseBodyHandler the response body handler
     * @return the response
     * @throws IOException              if an I/O error occurs when sending or receiving, or
     *                                  the client has shut down
     * @throws InterruptedException     if the operation is interrupted
     * @throws IllegalArgumentException if the {@code request} argument is not
     *                                  a request that could have been validly built as specified by {@link
     *                                  HttpRequest.Builder HttpRequest.Builder}.
     * @throws SecurityException        If a security manager has been installed
     *                                  and it denies {@link URLPermission access} to the
     *                                  URL in the given request, or proxy if one is configured.
     *                                  See <a href="#securitychecks">security checks</a> for further
     *                                  information.
     */
    @Override
    public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) throws IOException, InterruptedException {
        try {
            var response = client.send(request, responseBodyHandler);
            if (response.statusCode() == HttpStatus.UNAUTHORIZED.code) {
                throw new UnauthorizedException();
            }
            return response;
        } catch (UnauthorizedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Session Expired");
            alert.setContentText("Session Expired, please login again");
            alert.showAndWait();
            LocalStorage.remove("role");
            LocalStorage.remove("token");
            SceneNavigator.refreshNavbar();
            SidebarController.refreshSidebar();
            Platform.runLater(() -> SceneNavigator.loadContent(Paths.HOME));
            return null;
        }
    }

    /**
     * Sends the given request asynchronously using this client with the given
     * response body handler.
     *
     * <p> Equivalent to: {@code sendAsync(request, responseBodyHandler, null)}.
     *
     * @param request             the request
     * @param responseBodyHandler the response body handler
     * @return a {@code CompletableFuture<HttpResponse<T>>}
     * @throws IllegalArgumentException if the {@code request} argument is not
     *                                  a request that could have been validly built as specified by {@link
     *                                  HttpRequest.Builder HttpRequest.Builder}.
     */
    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) {
        return client.sendAsync(request, responseBodyHandler);
    }

    /**
     * Sends the given request asynchronously using this client with the given
     * response body handler and push promise handler.
     *
     * <p> The returned completable future, if completed successfully, completes
     * with an {@link HttpResponse}{@code <T>} that contains the response status,
     * headers, and body ( as handled by given response body handler ).
     *
     * <p> {@linkplain java.net.http.HttpResponse.PushPromiseHandler Push promises} received, if any, are
     * handled by the given {@code pushPromiseHandler}. A {@code null} valued
     * {@code pushPromiseHandler} rejects any push promises.
     *
     * <p> The returned completable future completes exceptionally with:
     * <ul>
     * <li>{@link IOException} - if an I/O error occurs when sending or receiving,
     *      or the client has shut down.</li>
     * <li>{@link SecurityException} - If a security manager has been installed
     *          and it denies {@link URLPermission access} to the
     *          URL in the given request, or proxy if one is configured.
     *          See <a href="#securitychecks">security checks</a> for further
     *          information.</li>
     * </ul>
     *
     * <p> The default {@code HttpClient} implementation returns
     * {@code CompletableFuture} objects that are <em>cancelable</em>.
     * {@code CompletableFuture} objects {@linkplain CompletableFuture#newIncompleteFuture()
     * derived} from cancelable futures are themselves <em>cancelable</em>.
     * Invoking {@linkplain CompletableFuture#cancel(boolean) cancel(true)}
     * on a cancelable future that is not completed, attempts to cancel the HTTP exchange
     * in an effort to release underlying resources as soon as possible.
     * No guarantee is made as to exactly <em>when</em> the cancellation request
     * may be taken into account. In particular, the request might still get sent
     * to the server, as its processing might already have started asynchronously
     * in another thread, and the underlying resources may only be released
     * asynchronously.
     * <ul>
     *     <li>With HTTP/1.1, an attempt to cancel may cause the underlying connection
     *         to be closed abruptly.
     *     <li>With HTTP/2, an attempt to cancel may cause the stream to be reset.
     * </ul>
     *
     * @param request             the request
     * @param responseBodyHandler the response body handler
     * @param pushPromiseHandler  push promise handler, may be null
     * @return a {@code CompletableFuture<HttpResponse<T>>}
     * @throws IllegalArgumentException if the {@code request} argument is not
     *                                  a request that could have been validly built as specified by {@link
     *                                  HttpRequest.Builder HttpRequest.Builder}.
     */
    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler, HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
        return client.sendAsync(request, responseBodyHandler, pushPromiseHandler);
    }
}
