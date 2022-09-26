package Images;

import java.util.HashMap;


public class words{
    public static void main(String [] args){
        // Set text in a string
        String text = "Hi! How are you? How old are you? Are u gonna talk with your mom? mom?";
        String[] input = text.split("[ \n\t\r,.;:!?(){}}]");//  {"Hi", "How", "are",  ...} == String[]

        HashMap <String, Integer> myMap  = new HashMap <> ();//{{"Abay zholy",10000}, {"Kyryk mysal",5000} , ...}

        for (int i = 0; i < input.length; i++) { // i = 1; i < 16

            String txt = input[i].toLowerCase(); // "Hi".toLowerCase() == "hi" ASCII a-97 A-65  B < b ?  b > a ?
                                                // "How" - > "how"

            if(input[i].length() > 1) {  // "hi".length() > 1 //true
                                        // "how".length() > 1 //true 3 > 1

                if(myMap.get(txt) == null) { // {{null, 0}, {null, 0} , ...} // "hi" == null
                                            // {{"hi", 1}, {null, 0} , ...} // "how" == null
                                            // "how" == 1 != null

                    myMap.put(txt, 1);   // {{"hi", 1}, ...}
                                        // {{"hi", 1}, {"how", 1} , ...}

                }
                else {

                    int value = myMap.get(txt).intValue(); // {"how", 1} // value = 1;

                    value++;

                    // value = 1+1 = 2

                    myMap.put(txt, value);

                }
            }
        }

        for(HashMap.Entry<String, Integer> nasii : myMap.entrySet()) { // {{"hi", 1}, {"how", 1} , ...} // nasii = {"hi", 1}

            System.out.println(
                    nasii.getKey() + " : " + nasii.getValue()
                    // hi           // :     // 1
                    //hi : 1
                    //redblueyellow
                    //2 + 2
                    //22
            );
        }
    }
}