package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

abstract class Player {
    //Játékos neve
    private String name;
    //Játékos neme true=férfi false=nő
    private boolean man;
    //Játékos nyert köreinek száma
    private int wonGames;
    //Játékos egy körben dobott értékei
    private List<Integer> playerThrows=new ArrayList<>();

    //Pár1
    private int pairOne;
    //Terc
    private int terc;
    //Pár2
    private int pairTwo;
    //Kissor
    private  boolean smallSeries;
    //bigSeries
    private  boolean bigSeries;
    //full
    private boolean full;
    //smallPoker
    private  int smallPoker;
    //bigPoker
    private  int bigPoker;

    //Legnagyobb dobás amely nem részepárnak vagy tercnek vagy kis és nagy pókernek
    private  int biggest;

    public int getBiggest() {
        return biggest;
    }

    public void setBiggest() {
        int max=0;
        for (int tmp : playerThrows) {
            if (tmp != pairOne && tmp != terc && tmp != pairTwo && tmp != smallPoker) {
                max = tmp;
            }
        }
        biggest=max;
    }


    //Játékos pontjainak növelése
    public void Increase()
    {
        wonGames++;
    }
    //Megadfja hogy egy játékosnak hány párja van
    public int pair(){
        int countOfPar=0;
        List<Integer> tmpThrows = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            if (Collections.frequency(playerThrows, i)==2 && tmpThrows.contains(i)==false){
                countOfPar++;
                tmpThrows.add(i);
            }
        }
        return  countOfPar;
    }
    //Beállítja a terc értéket attól függően hogy van e vagy nincs.Ha nincs -1 lesz a terc értéke
    public void setTerc()
    {
        boolean found=false;
        for (int i = 0; i < playerThrows.size(); i++) {
            if (Collections.frequency(playerThrows, playerThrows.get(i))==3){
                terc = playerThrows.get(i);
                found=true;
            }
        }
        if(found==false)
        {
            terc=-1;
        }
    }
    //A két pár beállítása.HA csak egy pár van a pair1 kapja az értéket ha kettő akkor a pair2 kapja a nagyobb értéket.
    public void setPairs()
    {
        if (pair()==1)
        {
            pairTwo=-1;
            for (int i = 0; i < playerThrows.size(); i++) {
                for (int j = 0; j < playerThrows.size(); j++) {
                    if(playerThrows.get(i)==playerThrows.get(j) && i!=j)
                    {
                        pairOne= playerThrows.get(i);
                    }
                }
            }
        }
        else if(pair()==2)
        {
            int whichPair=0;
            for (int i = 0; i < playerThrows.size()-1; i++) {
                if (playerThrows.get(i)==playerThrows.get(i+1))
                {
                    whichPair++;
                    if(whichPair==1)
                    {
                        pairOne= playerThrows.get(i);
                    }
                    else if(whichPair==2)
                    {
                        pairTwo= playerThrows.get(i);
                    }
                }
            }
        }
        else
        {
            pairOne=-1;
            pairTwo=-1;
        }
    }
    //Megadja hogy a játékosnak van e kis sora
    public  void setSmallSeries()
    {
        for (int i = 0; i < 5; i++) {
            if (playerThrows.get(i) != i + 1) {
                smallSeries = false;
            }
        }
        smallSeries=true;
    }
    //Megadja hogy a játékosnak van e nagy sora
    public   void setBigSeries()
    {
        for (int i = 1; i < 6; i++) {
            if (playerThrows.get(i-1)!=i+1){
                smallSeries=false;
                return;
            }
        }
        smallSeries=true;
    }
    //Megadja hogy a játékosnak van e fullja
    public void full()
    {
        if (pairOne!=-1 && terc!=-1)
        {
            full= true;
        }
        else {
            full= false;
        }
    }
    //Beállítja a kis pókernek az értékét
    public   void setSmallPoker()
    {
        for (int i = 0; i < playerThrows.size(); i++) {
            int counter=0;
            int tmp=0;
            for (int j = 0; j < playerThrows.size(); j++) {
                if (playerThrows.get(i)==playerThrows.get(j)){
                    counter++;
                    tmp=playerThrows.get(i);
                }
            }
            if (counter==4){
                smallPoker=tmp;
                return;
            }
        }
        smallPoker=-1;
    }
    //Beállítja a nagy pókernek az értékét
    public void setBigPoker()
    {
        for (int i = 0; i < playerThrows.size(); i++) {
            int bigPokerNum=0;
            int counter=0;
            for (int j = 0; j < playerThrows.size(); j++) {
                if (playerThrows.get(i)==playerThrows.get(j)){
                    counter++;
                    bigPokerNum=playerThrows.get(j);
                }
            }
            if (counter==5){
                bigPoker = bigPokerNum;
                return;
            }
        }
        bigPoker=-1;
    }
    //Megmondja hogy a játékosnak mi a legnagyobb sora/párja
    public int BiggestThing()
    {
        if(bigPoker!=-1)
        {
            return 0;
        }
        else if(smallPoker!=-1)
        {
            return 1;
        }
        else if(full)
        {
            return 2;
        }
        else if(bigSeries)
        {
            return 3;
        }
        else if(smallSeries)
        {
            return 4;
        }
        else if(pairTwo!=-1)
        {
            return 5;
        }
        else if(terc!=-1)
        {
            return 6;
        }
        return 7;
    }
    //Új értékeket állít be a játékosnak
    public void Reset()
    {
        this.setPlayerThrows();
        this.setPairs();
        this.setTerc();
        this.setSmallSeries();
        this.setBigSeries();
        this.full();
        this.setSmallPoker();
        this.setBigPoker();
        this.setBiggest();
    }
    //Beállítja a játékos dobásait és rendezi azokat növekvősorrendbe hogy könnyebb legyen vele dolgozni
    public void setPlayerThrows()
    {
        List<Integer> tmpThrows = new ArrayList<>();
        //Ideiglenes lista random számokkal való feltöltése
        for (int k = 0; k < 5; k++) {
            Random rnd = new Random();
            tmpThrows.add(rnd.nextInt(5) + 1);
        }
        //Sorba rendezem a playerThrows listát hogy később könnyebb legyen vizsgálni
        Collections.sort(tmpThrows);
        this.playerThrows = tmpThrows;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isMan()
    {
        return man;
    }

    public void setMan(boolean man)
    {
        this.man = man;
    }

    public int getWonGames()
    {
        return wonGames;
    }

    public void setWonGames(int wonGames)
    {
        this.wonGames = wonGames;
    }

    public List<Integer> getPlayerThrows()
    {
        return playerThrows;
    }

    public int getPairOne() {
        return pairOne;
    }

    public int getTerc() {
        return terc;
    }

    public int getPairTwo() {
        return pairTwo;
    }

    public boolean isSmallSeries() {
        return smallSeries;
    }

    public boolean isBigSeries() {
        return bigSeries;
    }

    public boolean isFull() {
        return full;
    }

    public int getSmallPoker() {
        return smallPoker;
    }

    public int getBigPoker() {
        return bigPoker;
    }
}

class Woman extends  Player{
    public void vasarolniMentem(){
        System.out.println("Vásárolni mentem!");
    }

    public Woman(String name,boolean man) {
        this.setName(name);
        this.setMan(man);
        this.setWonGames(0);
        this.setPlayerThrows();
        this.setPairs();
        this.setTerc();
        this.setSmallSeries();
        this.setBigSeries();
        this.full();
        this.setSmallPoker();
        this.setBigPoker();
        this.setBiggest();
    }
}

class Man extends  Player{
    public void mecsetNezek(){
        System.out.println("Hajrá magyarok!");
    }

    public Man(String name,boolean man) {
        this.setName(name);
        this.setMan(man);
        this.setWonGames(0);
        this.setPlayerThrows();
        this.setPairs();
        this.setTerc();
        this.setSmallSeries();
        this.setBigSeries();
        this.full();
        this.setSmallPoker();
        this.setBigPoker();
        this.setBiggest();
    }
}