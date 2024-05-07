import com.khanr1.cryocompose.wiring.rf.RfAssembly
import com.khanr1.cryocompose.Product
import com.khanr1.cryocompose.wiring.rf.RfConnector
import com.khanr1.cryocompose.Product
import com.khanr1.cryocompose.*
import io.github.iltotore.iron.*

val test = Category(5, CategoryName("test"), CategoryDescription("description"), None)

test.productIterator.toList

val list = classOf[RfAssembly[Int, Int, Int, Int]]
  .getDeclaredFields()
  .map { f =>
    f.getName()
  }
  .toList

list.head