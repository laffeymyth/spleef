package net.laffeymyth.spleef.api.localization;

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

        ComponentLocalizationService componentLocalizationService = new ComponentLocalizationService();
        componentLocalizationService.getLanguageMap().put("ru", ru);

        componentLocalizationService.getLanguageMap().values().forEach(localizationMessageSource -> {
            localizationMessageSource.getMessageMap().entrySet().forEach(stringStringEntry -> {
                log.info(stringStringEntry.getKey() + ":" + stringStringEntry.getValue());
            });
        });

        return componentLocalizationService;
    }

    private Reader getReaderByFileName(String fileName) {
        return new InputStreamReader(Objects.requireNonNull(plugin.getResource(fileName), "файл " + fileName + " не найден!"), StandardCharsets.UTF_8);
    }
}
