import squants.space.Millimeters
import com.khanr1.cryocompose.wiring.Hermeticity
import squants.time.Gigahertz
import com.khanr1.cryocompose.wiring.Gender
import com.khanr1.cryocompose.wiring.ConnectorName
import com.khanr1.cryocompose.wiring.rf.RfConnector
import com.khanr1.cryocompose.wiring.rf.RfBulkhead
import com.khanr1.cryocompose.stages.Stages
import com.khanr1.cryocompose.ports.Ports
import com.khanr1.cryocompose.wiring.rf.RfInstallationFlange

import io.circe.syntax.*

val rfConnectorState: Vector[RfConnector[Int, Int, Int]] = Vector(
  RfConnector(1, ConnectorName.applyUnsafe("SMA"), Gender.Female, Gigahertz(18), 2, Set(2)),
  RfConnector(2, ConnectorName.applyUnsafe("K"), Gender.Female, Gigahertz(40), 2, Set(2)),
)

val rfbulkheadState: Vector[RfBulkhead[Int, Int, Int]] =
  for
    connector <- rfConnectorState
    hermetic <- Hermeticity.values
  yield RfBulkhead(
    Integer.parseInt(
      rfConnectorState.indexOf(connector).toString() + Hermeticity.values.indexOf(hermetic) + 1,
      2,
    ),
    connector,
    Millimeters(10),
    hermetic,
    2,
    Set(2),
  )

val test =
  RfInstallationFlange(1, Ports.KF40, List.fill(7)(rfbulkheadState.head), Stages.RT_SL, 1, Set(2))

test.asJson.as[RfInstallationFlange[Int, Int, Int, Int]]
