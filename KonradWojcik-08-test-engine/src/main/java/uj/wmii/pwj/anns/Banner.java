package uj.wmii.pwj.anns;

import java.util.Map;

public class Banner {

    public String toBanner(String input) {
        Map<Character, String> bannerConverter = Map.ofEntries(
                Map.entry('A', """
                           #   \s
                          # #  \s
                         #   # \s
                        #     #\s
                        #######\s
                        #     #\s
                        #     #\s
                               """),
                Map.entry('B', """
                        ###### \s
                        #     #\s
                        #     #\s
                        ###### \s
                        #     #\s
                        #     #\s
                        ###### \s
                               """),
                Map.entry('C', """
                         ##### \s
                        #     #\s
                        #      \s
                        #      \s
                        #      \s
                        #     #\s
                         ##### \s
                        """),
                Map.entry('D', """
                        ###### \s
                        #     #\s
                        #     #\s
                        #     #\s
                        #     #\s
                        #     #\s
                        ###### \s
                        """),
                Map.entry('E', """
                        #######\s
                        #      \s
                        #      \s
                        #####  \s
                        #      \s
                        #      \s
                        #######\s
                        """),
                Map.entry('F', """
                        #######\s
                        #      \s
                        #      \s
                        #####  \s
                        #      \s
                        #      \s
                        #      \s
                        """),
                Map.entry('G', """
                         ##### \s
                        #     #\s
                        #      \s
                        #  ####\s
                        #     #\s
                        #     #\s
                         ##### \s
                        """),
                Map.entry('H', """
                        #     #\s
                        #     #\s
                        #     #\s
                        #######\s
                        #     #\s
                        #     #\s
                        #     #\s
                        """),
                Map.entry('I', """
                        ###\s
                         # \s
                         # \s
                         # \s
                         # \s
                         # \s
                        ###\s
                        """),
                Map.entry('J', """
                              #\s
                              #\s
                              #\s
                              #\s
                        #     #\s
                        #     #\s
                         ##### \s
                        """),
                Map.entry('K', """
                        #    #\s
                        #   # \s
                        #  #  \s
                        ###   \s
                        #  #  \s
                        #   # \s
                        #    #\s
                        """),
                Map.entry('L', """
                        #      \s
                        #      \s
                        #      \s
                        #      \s
                        #      \s
                        #      \s
                        #######\s
                        """),
                Map.entry('M', """
                        #     #\s
                        ##   ##\s
                        # # # #\s
                        #  #  #\s
                        #     #\s
                        #     #\s
                        #     #\s
                        """),
                Map.entry('N', """
                        #     #\s
                        ##    #\s
                        # #   #\s
                        #  #  #\s
                        #   # #\s
                        #    ##\s
                        #     #\s
                        """),
                Map.entry('O', """
                        #######\s
                        #     #\s
                        #     #\s
                        #     #\s
                        #     #\s
                        #     #\s
                        #######\s
                        """),
                Map.entry('P', """
                        ###### \s
                        #     #\s
                        #     #\s
                        ###### \s
                        #      \s
                        #      \s
                        #      \s
                        """),
                Map.entry('Q', """
                         ##### \s
                        #     #\s
                        #     #\s
                        #     #\s
                        #   # #\s
                        #    # \s
                         #### #\s
                        """),
                Map.entry('R', """
                        ###### \s
                        #     #\s
                        #     #\s
                        ###### \s
                        #   #  \s
                        #    # \s
                        #     #\s
                        """),
                Map.entry('S', """
                         ##### \s
                        #     #\s
                        #      \s
                         ##### \s
                              #\s
                        #     #\s
                         ##### \s
                        """),
                Map.entry('T', """
                        #######\s
                           #   \s
                           #   \s
                           #   \s
                           #   \s
                           #   \s
                           #   \s
                        """),
                Map.entry('U', """
                        #     #\s
                        #     #\s
                        #     #\s
                        #     #\s
                        #     #\s
                        #     #\s
                         ##### \s
                        """),
                Map.entry('V', """
                        #     #\s
                        #     #\s
                        #     #\s
                        #     #\s
                         #   # \s
                          # #  \s
                           #   \s
                        """),
                Map.entry('W', """
                        #     #\s
                        #  #  #\s
                        #  #  #\s
                        #  #  #\s
                        #  #  #\s
                        #  #  #\s
                         ## ## \s
                        """),
                Map.entry('X', """
                        #     #\s
                         #   # \s
                          # #  \s
                           #   \s
                          # #  \s
                         #   # \s
                        #     #\s
                        """),
                Map.entry('Y', """
                        #     #\s
                         #   # \s
                          # #  \s
                           #   \s
                           #   \s
                           #   \s
                           #   \s
                        """),
                Map.entry('Z', """
                        #######\s
                             # \s
                            #  \s
                           #   \s
                          #    \s
                         #     \s
                        #######\s
                        """),
                Map.entry(' ', """
                          \s
                          \s
                          \s
                          \s
                          \s
                          \s
                          \s
                        """)
        );
        if (input == null)
            return "";
        input = input.toUpperCase();
        int sizeFont = 7;
        String[] result = new String[sizeFont];
        for (int i = 0; i < sizeFont; i++)
            result[i] = "";

        for (int i = 0; i < input.length(); i++) {
            String letter = bannerConverter.get(input.charAt(i));
            String[] bannerConverterToNewLines = letter.split("\n");
            for (int j = 0; j < sizeFont; j++) {
                result[j] += bannerConverterToNewLines[j];
            }
        }
        StringBuilder resultString = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            resultString.append(result[i] + "\n");
        }
        return resultString.toString();
    }


}