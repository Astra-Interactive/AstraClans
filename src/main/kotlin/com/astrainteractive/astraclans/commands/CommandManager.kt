import com.astrainteractive.astraclans.commands.*
import com.astrainteractive.astraclans.commands.clan.ClanCommand


/**
 * Command handler for your plugin
 * It's better to create different executors for different commands
 * @see Reload
 */
class CommandManager {
    /**
     * Here you should declare commands for your plugin
     *
     * Commands stored in plugin.yml
     *
     * etemp has TabCompleter
     */
    init {
        tabCompleter()
        reload()
        ClanCommand()
    }


}