import com.khanr1.cryocompose.stages.SetStageLength
import com.khanr1.cryocompose.stages.SetStageLength.*

import cats.Show
import cats.syntax.all.*

SetStageLength.values.toList.map(_.show).foreach(println(_))
