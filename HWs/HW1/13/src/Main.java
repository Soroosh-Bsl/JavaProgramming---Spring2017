import java.util.ArrayList;
import java.util.Scanner;

class LanguageNewer
{
    String original = new String();
    String changed = new String();

    LanguageNewer(String word)
    {
        this.original = word;
        thRemover(this.original);
        zamirRemover(changed);
        sametRemover(changed);
        wRemover(changed);
        vovelRemover(changed);
        yRemover(changed);
    }

     void thRemover(String word)
    {
        if (word.startsWith("th"))
        {
            changed = "d" + word.substring(2, word.length());
        }
        else
            changed = original;
    }

    void zamirRemover(String word)
    {
        if (word.equals("he"))
            changed = "hit";
        else if (word.equals("it"))
            changed = "hit";
        else if (word.equals("she"))
            changed = "hit";
    }

    void sametRemover(String word)
    {

        int i = 0;
        String character = new String();
        for (i = 1; i < word.length() - 2; i++)
        {
            if (word.charAt(i) != 'a' && word.charAt(i) != 'o' && word.charAt(i) != 'e' && word.charAt(i) != 'u' && word.charAt(i) != 'i' && word.charAt(i) < 123 && word.charAt(i) > 96)
            {
                if (word.charAt(i + 1) == word.charAt(i))
                {
                    word = word.substring(0, i) + character + word.substring(i + 1, word.length());
                }
            }
        }
        changed = word;
    }

    void wRemover(String word)
    {
        int i =0;
        if (word.startsWith("where") || word.startsWith("when") || word.startsWith("what") || word.startsWith("who") || word.startsWith("whom") || word.startsWith("why") || word.startsWith("whose") || word.startsWith("how") || word.startsWith("which"));

        else
        {
            for (i = 0; i < word.length() - 1; i++)
            {
                if (word.charAt(i) == 'w')
                {
                    if (i == 0 || (word.charAt(i - 1) != 'a' && word.charAt(i - 1) != 'o' && word.charAt(i - 1) != 'i' && word.charAt(i - 1) != 'e' && word.charAt(i - 1) != 'u'))
                        word = word.substring(0, i) + "v" + word.substring(i+1, word.length());
                }

            }
        }
        changed = word;
    }

    void vovelRemover(String word)
    {
        int i = 0;
        for (i = 1; i < word.length(); i++)
        {
            if (word.charAt(i) == 'a' || word.charAt(i) == 'u' || word.charAt(i) == 'i' || word.charAt(i) == 'e' || word.charAt(i) == 'o')
            {
                if (word.charAt(i - 1) == 'a' || word.charAt(i - 1) == 'u' || word.charAt(i - 1) == 'i' || word.charAt(i - 1) == 'e' || word.charAt(i - 1) == 'o')
                {
                    word = word.substring(0, i) + word.substring(i - 1, i) + word.substring(i + 1, word.length());
                }
            }
        }
        changed = word;
    }

    void yRemover(String word)
    {
        if (word.charAt(word.length() - 1) == 'y')
            word = word.substring(0, word.length() - 1) + "i";

        changed = word;
    }
}

public class Main {

    public static void main(String[] args)
    {
        Scanner Scan = new Scanner(System.in);
        String text = new String();

        text = Scan.nextLine();

        int i = 0, j = 0;
        boolean flag = false;

        ArrayList<String> printed = new ArrayList<>();

        String[] word = new String[100000];
        LanguageNewer[] correction = new LanguageNewer[100000];
        for (String split : text.split(" "))
        {
            word[i] = new String();
            word[i] = split;
            if (word[i] == null)
                continue;
            word[i] = word[i].replace(",", "");
            word[i] = word[i].replace("?", "");
            word[i] = word[i].replace("!", "");
            word[i] = word[i].replace("\"", "");
            word[i] = word[i].replace("!", "");
            word[i] = word[i].replace("(", "");
            word[i] = word[i].replace(")", "");
            word[i] = word[i].replace(".", "");
            word[i] = word[i].toLowerCase();
            if (word[i].length() < 2)
                continue;
            correction[i] = new LanguageNewer(word[i]);

           if (!correction[i].changed.equals(correction[i].original))
            {

                if(flag && !printed.contains(correction[i].original))
                    System.out.println();
                flag = true;
                if (!printed.contains(correction[i].original))
                    System.out.print(correction[i].original + " -> " + correction[i].changed);

                printed.add(correction[i].original);
            }
            i++;
        }
    }
}

