package pl.memexurer.kguild5.bukkit.util;

import org.bukkit.ChatColor;

public final class ChatHelper {

  private static final char GLOBAL_COLOR = 'a';
  private static final char ACCENT_COLOR = '2';

  private ChatHelper() {
  }

  public static String fixColor(String str) {
    char[] b = str.toCharArray();
    for (int i = 0; i < b.length - 1; i++) {
      if (b[i] == '&' && "0123456789AaBbCcDdEeFfGgHhKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
        b[i] = ChatColor.COLOR_CHAR;

        char c = b[i + 1];
        if (c == 'g' || c == 'G') {
          b[i + 1] = GLOBAL_COLOR;
        } else if (c == 'H' || c == 'h') {
          b[i + 1] = ACCENT_COLOR;
        } else {
          b[i + 1] = Character.toLowerCase(c);
        }
      }
    }
    return new String(b);
  }
}
