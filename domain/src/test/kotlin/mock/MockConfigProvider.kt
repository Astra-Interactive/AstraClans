package mock

import com.astrainteractive.astraclans.domain.config.PluginConfig
import com.astrainteractive.astraclans.domain.di.IConfigProvider

object MockConfigProvider : IConfigProvider() {
    override fun initializer(): PluginConfig = PluginConfig()
}