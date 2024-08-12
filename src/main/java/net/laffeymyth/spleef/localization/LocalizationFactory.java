package net.laffeymyth.spleef.localization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.localization.commons.service.LocalizationMessageSource;
import net.laffeymyth.localization.commons.service.MessageParser;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class LocalizationFactory {
    private final Plugin plugin;

    public ComponentLocalizationService create() throws FileNotFoundException {
        LocalizationMessageSource ru = new LocalizationMessageSource();

        MessageParser messageParser = new MessageParser();
        messageParser.parse(ru, getReaderByFileName("waiting.json"));
        messageParser.parse(ru, getReaderByFileName("game.json"));
        messageParser.parse(ru, getReaderByFileName("credentials.json"));
        messageParser.parse(ru, getReaderByFileName("game_api.json"));
        messageParser.parse(ru, getReaderByFileName("words.json"));

        ComponentLocalizationService componentLocalizationService = new ComponentLocalizationService();
        componentLocalizationService.getLanguageMap().put("ru", ru);

        return componentLocalizationService;
    }

    private Reader getReaderByFileName(String fileName) {
        return new InputStreamReader(Objects.requireNonNull(plugin.getResource(fileName), "файл " + fileName + " не найден!"), StandardCharsets.UTF_8);
    }
}
