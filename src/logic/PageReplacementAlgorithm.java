package logic;

import java.util.ArrayList;
import java.util.Collections;

public class PageReplacementAlgorithm {
    final private int frames;
    private int quantBitR = 0;
    ArrayList<String> pagesString;
    ArrayList<Page> pagesPage;
    ArrayList<String> processMemoryString;
    ArrayList<Page> processMemoryPage;

    public PageReplacementAlgorithm(int frames, int quantBitR) {
        this.frames = frames;
        this.quantBitR = quantBitR;
        pagesPage = SetPageList.pagesRW;
        processMemoryPage = new ArrayList<>(frames);
    }

    public PageReplacementAlgorithm(int frames) {
        this.frames = frames;
        pagesString = SetPageList.pages;
        processMemoryString = new ArrayList<>(frames);
    }

    public void fifo() {
        int pageHit = 0;
        int pageFault = 0;
        for (int i = 0; i < pagesString.size(); i++){
            if(processMemoryString.size() != (frames)){
                if(processMemoryString.contains(pagesString.get(i))) pageHit++;
                else {
                    processMemoryString.add(pagesString.get(i));
                    System.out.println(processMemoryString);
                    pageFault++;
                }
            }else{
                if(processMemoryString.contains(pagesString.get(i))) {
                    pageHit++;
                }
                else{
                    pageFault++;
                    processMemoryString.set(0, pagesString.get(i));
                    Collections.rotate(processMemoryString, -1);
                    System.out.println(processMemoryString);
                }
            }
        }
        System.out.println("----------- FIFO -----------");
        System.out.println("Acertos: " + pageHit);
        System.out.println("Faltas: " + pageFault);
        System.out.println("----------- Processos na memória -----------");
        processMemoryString.forEach(System.out::println);
    }

    public void secondChance () {
        int pageHit = 0;
        int pageFault = 0;
        for (String s : pagesString) {
            if (processMemoryString.size() != (frames)) {
                if (processMemoryString.contains(s)) pageHit++;
                else {
                    processMemoryString.add(s);
                    System.out.println(processMemoryString);
                    pageFault++;
                }
            } else {
                if (processMemoryString.contains(s)) {
                    pageHit++;
                } else {
                    pageFault++;
                    processMemoryString.set(0, s);
                    Collections.rotate(processMemoryString, -1);
                    System.out.println(processMemoryString);
                }
            }
        }
        System.out.println("----------- FIFO -----------");
        System.out.println("Acertos: " + pageHit);
        System.out.println("Faltas: " + pageFault);
        System.out.println("----------- Processos na memória -----------");
        processMemoryString.forEach(System.out::println);
    }



}
