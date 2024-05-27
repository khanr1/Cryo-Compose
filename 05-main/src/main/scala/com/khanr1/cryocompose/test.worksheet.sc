import com.khanr1.cryocompose.wiring.rf.RfSet
import com.khanr1.cryocompose.wiring.rf.RFmaterial
import com.khanr1.cryocompose.wiring.Material
import com.khanr1.cryocompose.stages.*

import com.khanr1.cryocompose.InitialState.rfAssemblyState
// we first get all the setstageLength
val stageSetLengths = SetStageLength.values.toVector
// then we get all the stageLenght from each Stage
val StageLengths = stageSetLengths.map(_.segments)
// then for each we look at the RF assembly that are compatible with this stage length for each
def mapStageLengthsToRFAssemblies(
  material: RFmaterial
) =
  StageLengths.map { stageLengths =>
    stageLengths.flatMap { stageLength =>
      rfAssemblyState.filter(rfAssembly =>
        rfAssembly.line.wire.length == stageLength && rfAssembly.line.wire.material == material
      )
    }
  }

RFmaterial
  .values
  .toList
  .flatMap(mapStageLengthsToRFAssemblies(_))
  .filterNot(_ == Nil)
  .map(RfSet(1, _, 2, Set(1, 2)))
  .map(_.rfAssemblies.map(_.id))
  .length
