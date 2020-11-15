import java.util.Scanner;

class PlayerX
{
    int[] s = new int[3];
    int[] r = new int[3];
    int g1;
    int g2;

    int detectMove (int[] r, int[] s, int g1, int g2, EmptyHouse[] emptyHouses, int q, String[][] map) {
        int i = 0, j = 0, k = 0, bestY = 3, bestX = 3, whichFlag = 0;
        boolean[] noConditionMet = new boolean[3];
        boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false;
        EmptyHouse[] canBeFilled = new EmptyHouse[20];
        for (i = 0; i < 3; i++) {
            if (this.r[i] == 2 || this.s[i] == 2 || this.g1 == 2 || this.g2 == 2)
            {
                if (this.r[i] == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].y == i) {
                            flag1 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 1;
                            k++;
                        }
                    }
                }
                if (this.s[i] == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == i) {
                            flag1 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 1;
                            k++;
                        }
                    }
                }
                if (this.g1 == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == emptyHouses[j].y) {
                            flag1 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 1;
                            k++;
                        }
                    }
                }
                if (this.g2 == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x + emptyHouses[j].y == 2) {
                            flag1 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 1;
                            k++;
                        }
                    }
                }
            }
            if ((r[i] == 2 || s[i] == 2 || g1 == 2 || g2 == 2)) {
                if (r[i] == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].y == i) {
                            flag2 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 2;
                            k++;
                        }
                    }
                }
                if (s[i] == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == i) {
                            flag2 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 2;
                            k++;
                        }
                    }
                }
                if (g1 == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == emptyHouses[j].y) {
                            flag2 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 2;
                            k++;
                        }
                    }
                }
                if (g2 == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x + emptyHouses[j].y == 2) {
                            flag2 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 2;
                            k++;
                        }
                    }
                }
            }
            if ((this.r[i] == 1 || this.s[i] == 1 || this.g1 == 1 || this.g2 == 1)) {
                if (this.r[i] == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].y == i) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[i][1].equals("(X)")) || (emptyHouses[j].x == 1 && (map[i][0].equals("(X)") || map[i][2].equals("(X)"))))
                            {
                                flag3 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 3;
                                k++;
                            }
                        }
                    }
                }
                if (this.s[i] == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == i)
                        {
                            if (((emptyHouses[j].y == 0 || emptyHouses[j].y == 2) && map[1][i].equals("(X)")) || (emptyHouses[j].y == 1 && (map[0][i].equals("(X)") || map[2][i].equals("(X)"))))
                            {
                                flag3 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 3;
                                k++;
                            }
                        }
                    }
                }
                if (this.g1 == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == emptyHouses[j].y) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[1][1].equals("(X)")) || (emptyHouses[j].x == 1 && (map[0][0].equals("(X)") || map[2][2].equals("(X)"))))
                            {
                                flag3 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 3;
                                k++;
                            }
                        }
                    }
                }
                if (this.g2 == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x + emptyHouses[j].y == 2) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[1][1].equals("(X)")) || (emptyHouses[j].x == 1 && (map[2][0].equals("(X)") || map[0][2].equals("(X)"))))
                            {
                                flag3 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 3;
                                k++;
                            }
                        }
                    }
                }
            }
            if ((r[i] == 1 || s[i] == 1 || g1 == 1 || g2 == 1)) {
                if (r[i] == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].y == i) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[i][1].equals("(O)")) || (emptyHouses[j].x == 1 && (map[i][0].equals("(O)") || map[i][2].equals("(O)"))))
                            {
                                flag4 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 4;
                                k++;
                            }
                        }
                    }
                }
                if (s[i] == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == i) {
                            if (((emptyHouses[j].y == 0 || emptyHouses[j].y == 2) && map[1][i].equals("(O)")) || (emptyHouses[j].y == 1 && (map[0][i].equals("(O)") || map[2][i].equals("(O)"))))
                            {
                                flag4 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 4;
                                k++;
                            }
                        }
                    }
                }
                if (g1 == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == emptyHouses[j].y) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[1][1].equals("(O)")) || (emptyHouses[j].x == 1 && (map[0][0].equals("(O)") || map[2][2].equals("(O)"))))
                            {
                                flag4 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 4;
                                k++;
                            }
                        }
                    }
                }
                if (g2 == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x + emptyHouses[j].y == 2) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[1][1].equals("(O)")) || (emptyHouses[j].x == 1 && (map[2][0].equals("(O)") || map[0][2].equals("(O)"))))
                            {
                                flag4 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 4;
                                k++;
                            }
                        }
                    }
                }
            }
            if (!flag1 && !flag2 && !flag3 && !flag4)
            {
                noConditionMet[i] = true;
            }
        }
        if (noConditionMet[0] && noConditionMet[1] && noConditionMet[2]) {
            for (i = 0; i < q; i++) {
                if (emptyHouses[i].y < bestY) {
                    bestY = emptyHouses[i].y;
                    bestX = emptyHouses[i].x;
                } else if (emptyHouses[i].y == bestY) {
                    if (emptyHouses[i].x < bestX) {
                        bestY = emptyHouses[i].y;
                        bestX = emptyHouses[i].x;
                    }
                }
            }
        }
        else
        {
            if (flag1)
                whichFlag = 1;
            else if (flag2)
                whichFlag = 2;
            else if (flag3)
                whichFlag = 3;
            else if (flag4)
                whichFlag = 4;
            for (i = 0; i < k; i++)
            {
//                System.out.println(canBeFilled[i].x +""+ canBeFilled[i].y +" type = "+ canBeFilled[i].type);
                if (canBeFilled[i].y < bestY && canBeFilled[i].type == whichFlag)
                {
                    bestY = canBeFilled[i].y;
                    bestX = canBeFilled[i].x;
                }
                else if (canBeFilled[i].y == bestY && canBeFilled[i].type == whichFlag)
                {
                    if (canBeFilled[i].x < bestX)
                    {
                        bestY = canBeFilled[i].y;
                        bestX = canBeFilled[i].x;
                    }
                }
            }
        }

        EmptyHouse result = new EmptyHouse();
        result.x = bestX;
        result.y = bestY;

        for (i = 0; i < 3; i++) {
            if (i != 0)
                System.out.println();
            for (j = 0; j < 3; j++) {
                if ((i != result.y) || (j != result.x)) {
                    System.out.print(map[i][j]);
                } else {
                    System.out.print("(X)");
                }
                if (j != 2)
                    System.out.print(" ");
            }
        }

        if (flag1)
        {
            System.out.println();
            System.out.print("X wins!");
        }
        return 1;
    }
}

class PlayerO
{
    int[] s = new int[3];
    int[] r = new int[3];
    int g1;
    int g2;

    int detectMove (int[] r, int[] s, int g1, int g2, EmptyHouse[] emptyHouses, int q, String[][] map) {
        int i = 0, j = 0, k = 0, bestY = 3, bestX = 3, whichFlag = 0;
        boolean[] noConditionMet = new boolean[3];
        boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false;
        EmptyHouse[] canBeFilled = new EmptyHouse[20];
        for (i = 0; i < 3; i++) {
            if (this.r[i] == 2 || this.s[i] == 2 || this.g1 == 2 || this.g2 == 2)
            {
                if (this.r[i] == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].y == i) {
                            flag1 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 1;
                            k++;
                        }
                    }
                }
                if (this.s[i] == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == i) {
                            flag1 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 1;
                            k++;
                        }
                    }
                }
                if (this.g1 == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == emptyHouses[j].y) {
                            flag1 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 1;
                            k++;
                        }
                    }
                }
                if (this.g2 == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x + emptyHouses[j].y == 2) {
                            flag1 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 1;
                            k++;
                        }
                    }
                }
            }
            if ((r[i] == 2 || s[i] == 2 || g1 == 2 || g2 == 2)) {
                if (r[i] == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].y == i) {
                            flag2 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 2;
                            k++;
                        }
                    }
                }
                if (s[i] == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == i) {
                            flag2 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 2;
                            k++;
                        }
                    }
                }
                if (g1 == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == emptyHouses[j].y) {
                            flag2 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 2;
                            k++;
                        }
                    }
                }
                if (g2 == 2) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x + emptyHouses[j].y == 2) {
                            flag2 = true;
                            canBeFilled[k] = new EmptyHouse();
                            canBeFilled[k].x = emptyHouses[j].x;
                            canBeFilled[k].y = emptyHouses[j].y;
                            canBeFilled[k].type = 2;
                            k++;
                        }
                    }
                }
            }
            if ((this.r[i] == 1 || this.s[i] == 1 || this.g1 == 1 || this.g2 == 1)) {
                if (this.r[i] == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].y == i) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[i][1].equals("(O)")) || (emptyHouses[j].x == 1 && (map[i][0].equals("(O)") || map[i][2].equals("(O)"))))
                            {
                                flag3 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 3;
                                k++;
                            }
                        }
                    }
                }
                if (this.s[i] == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == i)
                        {
                            if (((emptyHouses[j].y == 0 || emptyHouses[j].y == 2) && map[1][i].equals("(O)")) || (emptyHouses[j].y == 1 && (map[0][i].equals("(O)") || map[2][i].equals("(O)"))))
                            {
                                flag3 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 3;
                                k++;
                            }
                        }
                    }
                }
                if (this.g1 == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == emptyHouses[j].y) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[1][1].equals("(O)")) || (emptyHouses[j].x == 1 && (map[0][0].equals("(O)") || map[2][2].equals("(O)"))))
                            {
                                flag3 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 3;
                                k++;
                            }
                        }
                    }
                }
                if (this.g2 == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x + emptyHouses[j].y == 2) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[1][1].equals("(O)")) || (emptyHouses[j].x == 1 && (map[2][0].equals("(O)") || map[0][2].equals("(O)"))))
                            {
                                flag3 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 3;
                                k++;
                            }
                        }
                    }
                }
            }
            if ((r[i] == 1 || s[i] == 1 || g1 == 1 || g2 == 1)) {
                if (r[i] == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].y == i) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[i][1].equals("(X)")) || (emptyHouses[j].x == 1 && (map[i][0].equals("(X)") || map[i][2].equals("(X)"))))
                            {
                                flag4 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 4;
                                k++;
                            }
                        }
                    }
                }
                if (s[i] == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == i) {
                            if (((emptyHouses[j].y == 0 || emptyHouses[j].y == 2) && map[1][i].equals("(X)")) || (emptyHouses[j].y == 1 && (map[0][i].equals("(X)") || map[2][i].equals("(X)"))))
                            {
                                flag4 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 4;
                                k++;
                            }
                        }
                    }
                }
                if (g1 == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x == emptyHouses[j].y) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[1][1].equals("(X)")) || (emptyHouses[j].x == 1 && (map[0][0].equals("(X)") || map[2][2].equals("(X)"))))
                            {
                                flag4 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 4;
                                k++;
                            }
                        }
                    }
                }
                if (g2 == 1) {
                    for (j = 0; j < q; j++) {
                        if (emptyHouses[j].x + emptyHouses[j].y == 2) {
                            if (((emptyHouses[j].x == 0 || emptyHouses[j].x == 2) && map[1][1].equals("(X)")) || (emptyHouses[j].x == 1 && (map[2][0].equals("(X)") || map[0][2].equals("(X)"))))
                            {
                                flag4 = true;
                                canBeFilled[k] = new EmptyHouse();
                                canBeFilled[k].x = emptyHouses[j].x;
                                canBeFilled[k].y = emptyHouses[j].y;
                                canBeFilled[k].type = 4;
                                k++;
                            }
                        }
                    }
                }
            }
            if (!flag1 && !flag2 && !flag3 && !flag4)
            {
                noConditionMet[i] = true;
            }
        }
        if (noConditionMet[0] && noConditionMet[1] && noConditionMet[2]) {
            for (i = 0; i < q; i++) {
                if (emptyHouses[i].y < bestY) {
                    bestY = emptyHouses[i].y;
                    bestX = emptyHouses[i].x;
                } else if (emptyHouses[i].y == bestY) {
                    if (emptyHouses[i].x < bestX) {
                        bestY = emptyHouses[i].y;
                        bestX = emptyHouses[i].x;
                    }
                }
            }
        }
        else
        {
            if (flag1)
                whichFlag = 1;
            else if (flag2)
                whichFlag = 2;
            else if (flag3)
                whichFlag = 3;
            else if (flag4)
                whichFlag = 4;
            for (i = 0; i < k; i++)
            {
//                System.out.println(canBeFilled[i].x +""+ canBeFilled[i].y +" type = "+ canBeFilled[i].type);
                if (canBeFilled[i].y < bestY && canBeFilled[i].type == whichFlag)
                {
                    bestY = canBeFilled[i].y;
                    bestX = canBeFilled[i].x;
                }
                else if (canBeFilled[i].y == bestY && canBeFilled[i].type == whichFlag)
                {
                    if (canBeFilled[i].x < bestX)
                    {
                        bestY = canBeFilled[i].y;
                        bestX = canBeFilled[i].x;
                    }
                }
            }
        }

        EmptyHouse result = new EmptyHouse();
        result.x = bestX;
        result.y = bestY;

        for (i = 0; i < 3; i++) {
            if (i != 0)
                System.out.println();
            for (j = 0; j < 3; j++) {
                if ((i != result.y) || (j != result.x)) {
                    System.out.print(map[i][j]);
                } else {
                    System.out.print("(O)");
                }
                if (j != 2)
                    System.out.print(" ");
            }
        }

        if (flag1)
        {
            System.out.println();
            System.out.print("O wins!");
        }
        return 1;
    }
}

class EmptyHouse
{
    int x, y;
    int type;
}

public class Main {

    public static void main(String[] args)
    {
        Scanner Scan = new Scanner(System.in);
        int i = 0, j = 0, q = 0;
        int numberOfX = 0, numberOfO = 0;

        PlayerO plo = new PlayerO();
        PlayerX plx = new PlayerX();

        EmptyHouse[] emptyHouse = new EmptyHouse[9];
        String[][] map = new String[3][3];
        for (i = 0; i < 3; i++)
        {
            for (j = 0; j < 3; j++)
            {
                map[i][j] = new String();
                map[i][j] = Scan.next();

                if (map[i][j].contains("(X)"))
                {
                    numberOfX += 1;
                    plx.r[i] += 1;
                    plx.s[j] += 1;
                    if (i == j)
                        plx.g1 += 1;
                    if (i + j == 2)
                        plx.g2 += 1;
                }

                else if (map[i][j].contains("(O)"))
                {
                    numberOfO += 1;
                    plo.r[i] += 1;
                    plo.s[j] += 1;
                    if (i == j)
                        plo.g1 += 1;
                    if (i + j == 2)
                        plo.g2 += 1;
                }
                else
                {
                    emptyHouse[q] = new EmptyHouse();
                    emptyHouse[q].x = j;
                    emptyHouse[q].y = i;
                    q++;
                }

            }
        }

        if (numberOfO == numberOfX)
        {
            plx.detectMove(plo.r, plo.s, plo.g1, plo.g2, emptyHouse, q, map);
        }
        else if (numberOfO < numberOfX)
        {
            plo.detectMove(plx.r, plx.s, plx.g1, plx.g2, emptyHouse, q, map);
        }
    }
}
