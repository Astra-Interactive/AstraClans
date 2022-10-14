package com.astrainteractive.astraclans.config

import ru.astrainteractive.astralibs.file_manager.FileManager


/**
 * All plugin files such as config.yml and other should only be stored here!
 */
class Files {
    val configFile: FileManager =
        FileManager("config.yml")
}