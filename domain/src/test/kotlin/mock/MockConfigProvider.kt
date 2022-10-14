package mock

import com.astrainteractive.astraclans.domain.config.IConfigProvider
import com.astrainteractive.astraclans.domain.config.PluginConfig

object MockConfigProvider : IConfigProvider {
    override var config: PluginConfig = PluginConfig()
}