package ltd.guimc.lgzbot.plugin.command

import kotlinx.coroutines.launch
import ltd.guimc.lgzbot.plugin.PluginMain
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.Image.Key.isUploaded
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

object ACGCommand: SimpleCommand (
    owner = PluginMain,
    primaryName = "acg",
    description = "啊哈哈哈哈 acg图片来咯"
) {
    @OptIn(ConsoleExperimentalApi::class)
    @Handler
    fun CommandSender.onHandler() = ltd_guimc_command_acg()

    fun CommandSender.ltd_guimc_command_acg() = launch{
        if (subject == null || bot == null) {
            throw Exception("Oops, something is empty")
        }
        val httpclients = HttpClients.createDefault()
        val httpget = HttpGet("https://www.dmoe.cc/random.php")
        val response = httpclients.execute(httpget)
        if (response.statusLine.statusCode == 200) {
            val entity = response.entity
            if (entity != null) {
                val inputstream = entity.content
                if (inputstream != null) {
                    val image = subject!!.uploadImage(inputstream.toExternalResource())
                    if (image.isUploaded(bot!!)) {
                        subject?.sendMessage(image)
                        return@launch
                    }
                }
            }
        }
        subject?.sendMessage("Oops, something went wrong.")
    }
}