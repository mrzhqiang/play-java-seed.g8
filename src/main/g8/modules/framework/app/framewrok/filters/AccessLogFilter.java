package filters;

import akka.stream.Materializer;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

@Singleton
public final class AccessLogFilter extends Filter {

  private static final String LOG_FORMAT =
      "method=%s uri=%s remote-address=%s domain=%s query-string=%s referer=%s user-agent=[%s]";

  @Inject
  public AccessLogFilter(Materializer mat) {
    super(mat);
  }

  @Override
  public CompletionStage<Result> apply(
      Function<Http.RequestHeader, CompletionStage<Result>> next,
      Http.RequestHeader req) {
    String msg = String.format(LOG_FORMAT,
        req.method(),
        req.uri(),
        req.remoteAddress(),
        req.host(),
        req.queryString(),
        req.getHeader("referer"),
        req.getHeader("user-agent"));
    Logger.of("access").info(msg);
    return next.apply(req);
  }
}
