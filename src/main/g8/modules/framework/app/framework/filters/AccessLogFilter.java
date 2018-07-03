package framework.filters;

import akka.stream.Materializer;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.Logger;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

/**
 * 访问日志过滤器。
 * <p>
 * 参考：<a href="https://www.playframework.com/documentation/2.6.x/JavaHttpFilters">官方文档。</a>
 *
 * @author mrzhqiang
 */
@Singleton
public final class AccessLogFilter extends Filter {

  private static final String MESSAGE = "method={} uri={} elapsed {}ms and returned {}";

  @Inject
  public AccessLogFilter(Materializer mat) {
    super(mat);
  }

  @Override
  public CompletionStage<Result> apply(
      Function<Http.RequestHeader, CompletionStage<Result>> next,
      Http.RequestHeader req) {
    long startTime = System.currentTimeMillis();
    return next.apply(req).thenApply(result -> {
      long endTime = System.currentTimeMillis();
      long requestTime = endTime - startTime;

      Logger.of(AccessLogFilter.class)
          .info(MESSAGE, req.method(), req.uri(), requestTime, result.status());
      return result.withHeader("Request-Time", String.valueOf(requestTime));
    });
  }
}
