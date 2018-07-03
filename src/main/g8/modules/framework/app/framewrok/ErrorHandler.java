package framework;

import com.typesafe.config.Config;
import core.exception.ApplicationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;

@Singleton
public final class ErrorHandler extends DefaultHttpErrorHandler {

  @Inject
  public ErrorHandler(Config config,
      Environment environment,
      OptionalSourceMapper sourceMapper,
      Provider<Router> routes) {
    super(config, environment, sourceMapper, routes);
  }

  @Override
  public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
    ErrorResponse error;
    // probably play form validation error
    if (exception instanceof IllegalArgumentException) {
      error = ErrorResponse.badRequest((IllegalArgumentException) exception);
    } else if (exception instanceof IllegalStateException) {
      error = ErrorResponse.badRequest((IllegalStateException) exception);
    } else if (exception instanceof ApplicationException) {
      error = ErrorResponse.appException((ApplicationException) exception);
    } else if (exception.getCause() instanceof ApplicationException) {
      error = ErrorResponse.appException((ApplicationException) exception.getCause());
    } else {
      error = ErrorResponse.unknownError(exception);
    }
    return CompletableFuture.completedFuture(error.toJsonResult());
  }

  @Override
  protected CompletionStage<Result> onForbidden(Http.RequestHeader request, String message) {
    return super.onForbidden(request, message);
  }

  @Override
  public CompletionStage<Result> onClientError(Http.RequestHeader request, int statusCode,
      String message) {
    return super.onClientError(request, statusCode, message);
  }
}
