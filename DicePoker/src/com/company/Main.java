package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        //Ebben a listában tárolom az összes játékost
        List<Player> players= new ArrayList<>();
        //Játékosok számának bekérése
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Játékosok száma: ");

        String playersNumbersStr = reader.readLine();
        int playersNumbersInt=0;
        //Ellenörzöm hogy a játékosok száma egyáltalán konvertálható-e számmá
        if (tryParseInt(playersNumbersStr)==true){
            //Ha szám akkor ellenörzöm hogy 0 és 5 közé essen a szám
            if (Integer.parseInt(playersNumbersStr)>0 && Integer.parseInt(playersNumbersStr)<5){
                playersNumbersInt=Integer.parseInt(playersNumbersStr);
                //Ha nem megfelelő a szám akkor addig kérem be a játékosok számát amíg megfelelő számot nem ad
            }else {
                System.out.println("A játékosok számának nagyobbnak kell lennie 0-nál és kissebnek 5-nél");
                while (playersNumbersInt<=0 || playersNumbersInt>=5){
                    System.out.print("Játékosok száma: ");
                    playersNumbersStr=reader.readLine();
                    if (tryParseInt(playersNumbersStr)==true){
                        playersNumbersInt=Integer.parseInt(playersNumbersStr);
                    }
                }
            }
        }
        /*
        Ha nem számot ad akkor addig kérem be a játékosok számát amíg az nem szám és a szám nem eseik 0 és 5 közé
         */
        else {
            System.out.println("Nem megfelelő a bemeneti karakterlánc formátuma!");
            while (tryParseInt(playersNumbersStr)==false || (playersNumbersInt<=0 || playersNumbersInt>=5)){
                System.out.print("Játékosok száma: ");
                playersNumbersStr=reader.readLine();
                if (tryParseInt(playersNumbersStr)==true){
                    playersNumbersInt=Integer.parseInt(playersNumbersStr);
                }
            }
        }
        /*
        Ez a ciklus minden egyes játékostól bekéri az objektum létrehozásához szükséges adatokat
        majd ezután létrehozza az objektumokat
         */
        for (int i = 0; i < playersNumbersInt; i++) {
            System.out.println(i+1+". Játékos adatai:");
            System.out.print("Játékos neve: ");
            String playeName=reader.readLine();
            System.out.print("Játékos neme(f/n): ");
            String playerGender=reader.readLine();
            /*
            A nem bekérésnék leellemnörzöm hogy férfi vagy nő e amit f vagy n betűvel tud megadni a felhasználó
            Ha nem jól adja meg akkor addig kérem be tőle amíg nem ad megfelelő értéket
             */
            if(!playerGender.equals("f") && !playerGender.equals("n")){
                System.out.println("A játékos neme nő(n) vagy férfi(f) lehet");
                while (!playerGender.equals("f") && !playerGender.equals("n")){
                    System.out.print("Játékos neme(f/n): ");
                    playerGender=reader.readLine();
                }
            }
            //A megadott nemtől függően létrehozom a megfelelő objektumot és feltöltöm a listába
            if (playerGender.equals("f")){
                Man m = new Man(playeName,true);
                players.add(m);
            }
            else {
                Woman w = new Woman(playeName,false);
                players.add(w);
            }
        }
        //Ha négynél kevesebb játékos van akkor létrehozok NŐ nevű játékosokat akiket szintén belerakok a listába
        if (playersNumbersInt<4){
            for (int i = 0; i <4-playersNumbersInt ; i++) {
                Woman w = new Woman("Nő",false);
                players.add(w);
            }
        }
        for (int i = 0; i < 10; i++) {
            //Az adott forduló számát adja meg
            int turn=i+1;
            System.out.println(turn+".Forduló");
            System.out.println();
            //Egy ideiglenes lista amely abban segít hogy megtaláljam a kör győztesét/győzteseit
            List<Player> tmpPlayers=new ArrayList<>();
            //A kör győztesét/győzteseit tárolom el benne a forduló győztesének kiíratásához
            List<Player> roundWinners=new ArrayList<>();
            //Itt nézem hogy mi a legerősebb sor/pár a játékban
            int min=8;
            for (Player player : players) {
                if (player.BiggestThing() <= min) {
                    min = player.BiggestThing();
                }
            }
            //Az ideiglenes listba feltöltöm azokat a játékosokat akik rendelkeznek azzal a típusú sorral vagy párral
            for (Player player : players) {
                if (player.BiggestThing() == min) {
                    tmpPlayers.add(player);
                }
            }
            if(min==0)
            {
                //Nagypóker
                int max=0;
                //Legnagyobb nagypóker érték megtalálása
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getBigPoker() >= max) {
                        max = tmpPlayer.getBigPoker();
                    }
                }
                //Akik a legnagyobb nagypóker értékkel rendelkeznek azoknak növelem a pontjukat és belekerülnek a győztesek listájába
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getBigPoker() == max) {
                        tmpPlayer.Increase();
                        roundWinners.add(tmpPlayer);
                    }
                }
            }
            else if(min==1)
            {
                //Kis Póker
                int maxPoker=0;
                //Legnagyobb kispóker érték megtalálása
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getSmallPoker() >= maxPoker) {
                        maxPoker = tmpPlayer.getSmallPoker();
                    }
                }
                int maxThrow=0;
                /*
                Hogy minnél jobban tudjam kiszűrni a győztest az alapján is szűröm a játékosokat hogy mekkora az a dobásuk ami nem része
                a kispókernek
                 */
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getBiggest() > maxThrow && tmpPlayer.getSmallPoker() == maxPoker) {
                        maxThrow = tmpPlayer.getBiggest();
                    }
                }
                /*
                Akik a legnagyobb kispóker értékkel rendelkeznek és a legnagyobb dobással azoknak növelem a
                pontjukat és belekerülnek a győztesek listájába
                */
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getSmallPoker() == maxPoker && tmpPlayer.getBiggest() == maxThrow) {
                        tmpPlayer.Increase();
                        roundWinners.add(tmpPlayer);
                    }
                }
            }
            else  if(min==2)
            {
                //Full
                int maxTerc=0;
                //Legnagyobb terc érték megtalálása
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getTerc() > maxTerc) {
                        maxTerc = tmpPlayer.getTerc();
                    }
                }
                int maxPair=0;
                //Legnagyobb pár érték megtalálása
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getTerc() == maxTerc && maxPair < tmpPlayer.getPairOne()) {
                        maxPair = tmpPlayer.getPairOne();
                    }
                }
                //Akik a legnagyobb terccel és párral rendelkeznek növelem a pontszámukat és beleteszem őket a kör győztesek listájába
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getTerc() == maxTerc && maxPair == tmpPlayer.getPairOne()) {
                        tmpPlayer.Increase();
                        roundWinners.add(tmpPlayer);
                    }
                }
            }
            else if(min==3)
            {
                //Nagysor
                //Akik nagy sorral rendelkeznek növelem a pontjukat és belekerülnek a kör győztesek listájába
                for (Player tmpPlayer : tmpPlayers) {
                    tmpPlayer.Increase();
                    roundWinners.add(tmpPlayer);
                }
            }
            else  if(min==4)
            {
                //Kissor
                //Akik kis sorral rendelkeznek növelem a pontjukat és belekerülnek a kör győztesek listájába
                for (Player tmpPlayer : tmpPlayers) {
                    tmpPlayer.Increase();
                    roundWinners.add(tmpPlayer);
                }
            }
            else  if(min==5)
            {
                //Két pár
                int maxPair2=0;
                //Két párközül a nagyobbik megtalálása
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getPairTwo() >= maxPair2) {
                        maxPair2 = tmpPlayer.getPairTwo();
                    }
                }
                int maxPair1=0;
                //Két párközül a kissebbik megtalálása
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getPairOne() >= maxPair1 && tmpPlayer.getPairTwo() == maxPair2) {
                        maxPair1 = tmpPlayer.getPairOne();
                    }
                }
                //Legnagyobb nem pár dobás megtalálása
                int maxThrow=0;
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getPairOne() == maxPair1 && tmpPlayer.getPairTwo() == maxPair2 && tmpPlayer.getBiggest() > maxThrow) {
                        maxThrow = tmpPlayer.getBiggest();
                    }
                }
                //Győztes/győztesek megtalálása és annak pontszámának növelése majd belerakjuk a kör győztesek listájába
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getPairOne() == maxPair1 && tmpPlayer.getPairTwo() == maxPair2 && tmpPlayer.getBiggest() == maxThrow) {
                        tmpPlayer.Increase();
                        roundWinners.add(tmpPlayer);
                    }
                }
            }
            else  if(min==6)
            {
                //Terc
                int maxTerc=0;
                //Legnagyobb terc megtalálása
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getTerc() >= maxTerc) {
                        maxTerc = tmpPlayer.getTerc();
                    }
                }
                int maxThrow=0;
                //Legnagyobb dobás megtalálása
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getTerc() == maxTerc && tmpPlayer.getBiggest() > maxThrow) {
                        maxThrow = tmpPlayer.getBiggest();
                    }
                }
                //Győztes/győztesek megtalálása és annak pontszámának növelése majd belerakjuk a kör győztesek listájába
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getTerc() == maxTerc && tmpPlayer.getBiggest() == maxThrow) {
                        tmpPlayer.Increase();
                        roundWinners.add(tmpPlayer);
                    }
                }
            }
            else if(min==7)
            {
                //Pár
                int maxPair=0;
                //Legnagyobb pár megtalálása
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getPairOne() >= maxPair) {
                        maxPair = tmpPlayer.getPairOne();
                    }
                }
                int maxThrow=0;
                //Legnagyobb dobás megtalálása
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getPairOne() == maxPair && maxThrow < tmpPlayer.getBiggest()) {
                        maxThrow = tmpPlayer.getBiggest();
                    }
                }
                //Győztes/győztesek megtalálása és annak pontszámának növelése majd belerakjuk a kör győztesek listájába
                for (Player tmpPlayer : tmpPlayers) {
                    if (tmpPlayer.getPairOne() == maxPair && tmpPlayer.getBiggest() == maxThrow) {
                        tmpPlayer.Increase();
                        roundWinners.add(tmpPlayer);
                    }
                }
            }
            else
            {
                //Ha egy játkosnak sincs semmije akkor mindnek növelem a pontját és belekerülnek a kör győzteseinek listájába
                for (Player player : players) {
                    player.Increase();
                    roundWinners.add(player);
                }
            }
            //Játékosok dobásainak megjelenítése
            for (int j = 0; j < 4; j++) {
                StringBuilder line= new StringBuilder(players.get(j).getName() + ": ");
                for (int k = 0; k < 5; k++) {
                    line.append(players.get(j).getPlayerThrows().get(k));
                }
                System.out.println(line);
            }
            //Forduló győztese/győzteseinek a kiíratása
            if (roundWinners.size()>1)
            {
                System.out.println("A forduló nyertesei");
                for (Player winner : roundWinners) {
                    System.out.println(winner.getName());
                }
            }
            else
            {
                System.out.println("A forduló nyertese:");
                System.out.println(roundWinners.get(0).getName());
            }
            //Következő fordulóra lépés vagy az eredmény kiíratása
            if(i!=9)
            {
                System.out.println("A következő körhöz nyomjom ENTER-t");
                reader.readLine();
                for (Player player : players) {
                    player.Reset();
                }
            }
            else
            {
                System.out.println("Az eredmények kiíratásához nyomjom ENTER-t");
                reader.readLine();
            }
        }
        //Játék végén a pontok kiíratása
        System.out.println("Pontok:");
        Player winner = players.get(0);
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getWonGames());
            if (winner.getWonGames() < player.getWonGames()) {
                winner = player;
            }
        }
        //A játék győztese/győzteseinek meghatározása
        List<Player> finalWinners=new ArrayList<>();
        for (Player player : players) {
            if (player.getWonGames() == winner.getWonGames()) {
                finalWinners.add(player);
            }
        }
        //Győztes/győztesek kiíratása
        if(finalWinners.size()>1)
        {
            System.out.println("A nyertesek:");
            for (Player player : finalWinners) {
                System.out.println(player.getName());
            }
        }
        else
        {
            System.out.println("A nyertes:");
            System.out.println(finalWinners.get(0).getName());
        }
    }
    //Egy tryparse függvény amely a helyes bemenet vizsgálatára szolgál
    static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
