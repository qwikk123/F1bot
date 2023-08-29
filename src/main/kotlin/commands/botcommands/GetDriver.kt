package commands.botcommands

import model.Driver
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.utils.FileUpload
import utils.EmbedCreator
import java.util.*
import java.util.function.Consumer

/**
 * Class representing the /getdriver command.
 */
class GetDriver(name: String, description: String, val driverMap: HashMap<String, Driver>) : BotCommand(name, description) {

    /**
     * Creates an instance of GetDriver.
     * @param name This commands name
     * @param description This commands description
     * @param driverMap Map containing the drivers from this F1 season.
     */
    init {
        val choiceList = ArrayList<Command.Choice>()
        driverMap.values.forEach { choiceList.add(Command.Choice(it.name, it.driverId))}
        options.add(
            OptionData(
                OptionType.STRING,
                "drivername",
                "Select driver to get info from",
                true,
                false
            ).addChoices(choiceList)
        )
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val driverId: String = (event.getOption("drivername"))!!.asString
        val driver: Driver = driverMap[driverId]!!
        val inputStream = Objects.requireNonNull(
            javaClass.getResourceAsStream(("/driverimages/" + driver.code) + ".png"),
            "inputStream is null"
        )
        event.hook.sendMessageEmbeds(EmbedCreator.createDriverProfile(driver).build())
            .addFiles(FileUpload.fromData(inputStream, "driverImage.png"))
            .queue()
    }
}
