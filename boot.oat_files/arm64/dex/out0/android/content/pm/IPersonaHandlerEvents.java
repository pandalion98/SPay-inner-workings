package android.content.pm;

public interface IPersonaHandlerEvents {
    void onFirstBoot();

    void onInit();

    void onPersonaSwitch();

    void onStateChange();
}
