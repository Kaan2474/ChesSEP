package com.ChesSEP.ChesSEP.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CSVReader {



    public String[][] CSVto2DArr(String csvFile) {
        //String csvFile = "C:\\Users\\yusuf\\Downloads\\chess_puzzles.csv";

        List<String[]> dataArray = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the CSV line by commas
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                List<String> rowList = new ArrayList<>();

                while (tokenizer.hasMoreTokens()) {
                    rowList.add(tokenizer.nextToken());
                }

                // Convert List<String> to String[]
                String[] rowArray = rowList.toArray(new String[0]);
                dataArray.add(rowArray);
            }

            // Now, 'dataArray' is a List<String[]> containing the CSV data

            // Convert List<String[]> to a two-dimensional array
            String[][] resultArray = dataArray.toArray(new String[0][]);

            return  resultArray;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

    public int[][][] FENtoBoard(int puzzleNr){
        String[][] arr = CSVto2DArr("C:\\\\Users\\\\yusuf\\\\Downloads\\\\chess_puzzles.csv");
        int FENpos = 0;
        String FEN;


        int[][][] resultArr = new int[8][8][2];

        for (int i = 0; i < arr.length; i++) {
            if("FEN".equals(arr[0][i])){
                FENpos = i;
                break;
            }
        }

            FEN = arr[puzzleNr][FENpos];
            char[] chars = FEN.toCharArray();

            int spalte = 0;
            int zeile = 0;

            for(int j = 0; j < chars.length; j++){



                switch (chars[j]) {
                        case 'K':
                            resultArr[zeile][spalte][0] = 6;
                            resultArr[zeile][spalte][1] = 1;
                            spalte++;
                            break;

                        case 'Q':
                            resultArr[zeile][spalte][0] = 5;
                            resultArr[zeile][spalte][1] = 1;
                            spalte++;
                            break;

                        case 'R':
                            resultArr[zeile][spalte][0] = 2;
                            resultArr[zeile][spalte][1] = 1;
                            spalte++;
                            break;

                        case 'B':
                            resultArr[zeile][spalte][0] = 4;
                            resultArr[zeile][spalte][1] = 1;
                            spalte++;
                            break;

                        case 'N':
                            resultArr[zeile][spalte][0] = 3;
                            resultArr[zeile][spalte][1] = 1;
                            spalte++;
                            break;

                        case 'P':
                            resultArr[zeile][spalte][0] = 1;
                            resultArr[zeile][spalte][1] = 1;
                            spalte++;
                            break;

                        case 'k':
                            resultArr[zeile][spalte][0] = 6;
                            resultArr[zeile][spalte][1] = 2;
                            spalte++;
                            break;

                        case 'q':
                            resultArr[zeile][spalte][0] = 5;
                            resultArr[zeile][spalte][1] = 2;
                            spalte++;
                            break;

                        case 'r':
                            resultArr[zeile][spalte][0] = 2;
                            resultArr[zeile][spalte][1] = 2;
                            spalte++;
                            break;

                        case 'b':
                            resultArr[zeile][spalte][0] = 4;
                            resultArr[zeile][spalte][1] = 2;
                            spalte++;
                            break;

                        case 'n':
                            resultArr[zeile][spalte][0] = 3;
                            resultArr[zeile][spalte][1] = 2;
                            spalte++;
                            break;

                        case 'p':
                            resultArr[zeile][spalte][0] = 1;
                            resultArr[zeile][spalte][1] = 2;
                            spalte++;
                            break;

                        case '/':
                            zeile++;
                            spalte = 0;
                            break;

                        case '1':
                            spalte += 1;
                            break;

                        case '2':
                            spalte += 2;
                            break;

                        case '3':
                            spalte += 3;
                            break;

                        case '4':
                            spalte += 4;
                            break;

                        case '5':
                            spalte += 5;
                            break;

                        case '6':
                            spalte += 6;
                            break;

                        case '7':
                            spalte += 7;
                            break;

                        case '8':
                            spalte += 8;
                            break;
                    }
            }
        //}


        return resultArr;
    }

    public static void main(String[] args) {
        CSVReader reader = new CSVReader();
        int[][][] threeDArray = reader.FENtoBoard(1);

        for (int i = 0; i < threeDArray.length; i++) {
            for (int j = 0; j < threeDArray[i].length; j++) {
                for (int k = 0; k < threeDArray[i][j].length; k++) {
                    System.out.print(threeDArray[i][j][k] + " ");
                }
                System.out.println();  // Move to the next line for the next "j" index
            }
            System.out.println();  // Add an empty line between "i" indices
        }
    }

}
