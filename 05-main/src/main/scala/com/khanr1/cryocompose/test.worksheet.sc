import com.khanr1.cryocompose.wiring.*
import com.khanr1.cryocompose.wiring.rf.RfAssembly
import com.khanr1.cryocompose.wiring.rf.RfConnector
import com.khanr1.cryocompose.wiring.rf.RfSet
import com.khanr1.cryocompose.wiring.rf.RFmaterial
import com.khanr1.cryocompose.wiring.Material
import com.khanr1.cryocompose.stages.*

import com.khanr1.cryocompose.InitialState.*

def findRFSet(
  setstage: SetStageLength,
  rfMaterial: RFmaterial,
  rfConnector: ConnectorName,
): List[RfAssembly[Int, Int, Int, Int]] =
  val listStage: List[StageLength] = setstage.segments
  println(listStage)
  listStage.flatMap(l =>
    rfAssemblyState
      .toList
      .filter(as => as.line.wire.material == rfMaterial)
      .filter(as => as.line.connectorA.name == rfConnector)
      .filter(as => as.line.wire.stageLength == Some(l))
  )

(for
  material <- RFmaterial.values.toVector
  setstage <- SetStageLength.values.toVector
  connector <- rfConnectorState.map(_.name)
yield findRFSet(setstage, material, connector).map(a =>
  (a.line.wire.material, a.line.wire.stageLength, a.line.connectorA.name)
)).filterNot(_.isEmpty).length
