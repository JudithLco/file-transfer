import com.example.app.services.FileService
import com.example.app.services.FileService.deleteFile
import com.example.app.services.InfoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Clock.System.now
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object DeleteService {

    @OptIn(ExperimentalTime::class)
    fun deleteExpired(){
        val files = InfoService.listAllData()

        for (file in files){
            val expirationFile = Instant.parse(file.expirationTime)
            if (now() > expirationFile){
                println("Deleting ${file.id}")
                FileService.deleteFile(file.id)
            }
        }
    }

    fun deleteJob(){
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            while (true) {
                try {
                    deleteExpired()
                    println("Trying to delete files")
                }
                catch (e:Exception){
                    println(e.message)
                }
                delay(30.minutes)
            }
        }
    }
}