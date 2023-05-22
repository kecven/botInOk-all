package digital.moveto.botinok.model.service;

import digital.moveto.botinok.model.entities.Setting;
import digital.moveto.botinok.model.entities.enums.SettingKey;
import digital.moveto.botinok.model.repositories.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingService {

    private final SettingRepository settingRepository;

    @Autowired
    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public String getSetting(SettingKey key) {
        return settingRepository.findByKey(key.name())
                .map(Setting::getValue)
                .orElse(key.getDefaultValue());
    }

    public Integer getSettingAsInteger(SettingKey key) {
        String setting = getSetting(key);
        return setting != null ? Integer.parseInt(setting) : null;
    }

    public Long getSettingAsLong(SettingKey key) {
        String setting = getSetting(key);
        return setting != null ? Long.parseLong(setting) : null;
    }

    public Boolean getSettingAsBoolean(SettingKey key) {
        String setting = getSetting(key);
        return setting != null ? Boolean.parseBoolean(setting) : null;
    }

    public void setSetting(SettingKey key, String value) {
        Setting setting = settingRepository.findByKey(key.name())
                .orElse(new Setting());
        setting.setKey(key.name());
        setting.setValue(value);
        settingRepository.save(setting);
    }

    public void setSetting(SettingKey key, Boolean value) {
        setSetting(key, value.toString());
    }
}
