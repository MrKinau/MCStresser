/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.fishingbot.modules;

import lombok.Getter;
import systems.kinau.fishingbot.Stresser;

public abstract class Module {

    private boolean enabled = false;
    @Getter private Stresser stresser;

    public Module(Stresser stresser) {
        this.stresser = stresser;
    }

    public void enable() {
        this.enabled = true;
        onEnable();
    }

    public void disable() {
        this.enabled = false;
        onDisable();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public abstract void onEnable();

    public abstract void onDisable();

}
