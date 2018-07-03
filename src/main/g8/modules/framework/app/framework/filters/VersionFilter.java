package framework.filters;

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

  private final String apiVersion;
  private final Executor exec;

  @Inject
  public VersionFilter(Materializer mat, Config conf, Executor exec) {
    super(mat);
    this.apiVersion = conf.getString("app.version");
    this.exec = exec;
  }

  @Override
  public CompletionStage<Result> apply(
      Function<RequestHeader, CompletionStage<Result>> next,
      RequestHeader req) {
    return next.apply(req).thenApplyAsync(
        result -> result.withHeader("X-Version", apiVersion),
        exec);
  }
}
