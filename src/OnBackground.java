import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static java.util.logging.Level.OFF;
import static java.util.logging.Logger.getLogger;
import static org.jnativehook.GlobalScreen.*;
import static org.jnativehook.keyboard.NativeKeyEvent.getKeyText;


public class OnBackground implements NativeKeyListener {

    private final CountDownLatch mLatch = new CountDownLatch(1000000000); //10000000 key events untill exiting
    private static Converter converter;

    public static void main(final String[] args)
            throws NativeHookException, InterruptedException, IOException {

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        EngToHeb.Init();
        HebToEng.Init();
        setSystemTrayMenu();

        disableNativeHookLogger();
        registerNativeHook();

        final OnBackground ob = new OnBackground();
        addNativeKeyListener(ob);
        System.out.println("Listener attached.");
        ob.start();
        System.out.println("Listener detached.");
        removeNativeKeyListener(ob);
    }

    private static void setSystemTrayMenu() {
        Image image = Toolkit.getDefaultToolkit().getImage(OnBackground.class.getResource("/myIcon.png"));

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(image, "Converter", popup);
        trayIcon.setImageAutoSize(true);
        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem exitItem = new MenuItem("Exit");
        CheckboxMenuItem engToHeb = new CheckboxMenuItem("English to Hebrew");
        engToHeb.setState(true);
        CheckboxMenuItem hebToEng = new CheckboxMenuItem("Hebrew to English");

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        engToHeb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (engToHeb.getState()) {
                    converter = new EngToHeb();
                    hebToEng.setState(false);
                }
            }
        });

        hebToEng.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (hebToEng.getState()) {
                    converter = new HebToEng();
                    engToHeb.setState(false);
                }
            }
        });

        popup.add(engToHeb);
        popup.add(hebToEng);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    @Override
    public void nativeKeyPressed(final NativeKeyEvent e) {
        System.out.println("Pressed: " + getKeyText(e.getKeyCode()));
        mLatch.countDown();
    }

    @Override
    public void nativeKeyReleased(final NativeKeyEvent e) {
        System.out.println("Released: " + getKeyText(e.getKeyCode()));
        if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT) {
            System.out.println("Shift Pressed!!!");
            changeClipboard();
        }
    }

    @Override
    public void nativeKeyTyped(final NativeKeyEvent e) {
        System.out.println("Typed: " + e);
    }

    private void start() throws InterruptedException {
        System.out.println("Awaiting keyboard events.");
        mLatch.await(); //time limit for waiting
        System.out.println("All keyboard events have fired, exiting.");
    }


    private static void disableNativeHookLogger() {
        final Logger logger = getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(OFF);
        logger.setUseParentHandlers(false);
    }


    public void changeClipboard() {
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        try {
            if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String) t.getTransferData(DataFlavor.stringFlavor);
                text = converter.convert(text);
                System.out.println(text);
                StringSelection newText = new StringSelection(text);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(newText, null);
            }
        } catch (Exception e) {
        }
    }
}



