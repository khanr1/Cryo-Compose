import cats.effect.kernel.Resource
import org.http4s.server.Server

trait HttpServer[F[_]]:
  def serve: Resource[F, Server]
