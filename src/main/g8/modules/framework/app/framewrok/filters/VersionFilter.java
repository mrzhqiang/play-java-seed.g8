package filters;

import akka.stream.Materializer;
import com.typesafe.config.Config;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.mvc.Filter;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

@Singleton
public final class VersionFilter extends Filter {

  private final Config conf;
  private final Executor exec;

  @Inject
  public VersionFilter(Materializer mat, Config conf, Executor exec) {
    super(mat);
    this.conf = conf;
    this.exec = exec;
  }

  @Override
  public CompletionStage<Result> apply(
      Function<RequestHeader, CompletionStage<Result>> next,
      RequestHeader req) {
    // 不作为类变量是因为，需要能够在运行时修改并立即生效
    String apiVersion = conf.getString("app.version");
    return next.apply(req).thenApplyAsync(
        result -> result.withHeader("X-Version", apiVersion),
        exec);
  }
}
