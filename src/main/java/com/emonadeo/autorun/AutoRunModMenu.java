package com.emonadeo.autorun;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class AutoRunModMenu implements ModMenuApi, ConfigScreenFactory {
    @Override
    public String getModId() {
        return AutoRun.MODID;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this;
    }

    @Override
    public Screen create(Screen screen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(screen)
                .setTitle(new TranslatableText("title." + AutoRun.MODID + ".config"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("config." + AutoRun.MODID + ".general"));
        general.addEntry(entryBuilder.startIntField(new TranslatableText("config." + AutoRun.MODID + ".delayBuffer"), AutoRun.getDelayBuffer())
            .setDefaultValue(20)
            .setTooltip(new TranslatableText("config." + AutoRun.MODID + ".delayBuffer.description"))
            .setSaveConsumer(AutoRun::setDelayBuffer)
            .build());

        return builder.setSavingRunnable(() -> {
            AutoRun.saveConfig(AutoRun.CFG_FILE);
            AutoRun.loadConfig(AutoRun.CFG_FILE);
        }).build();
    }
}
