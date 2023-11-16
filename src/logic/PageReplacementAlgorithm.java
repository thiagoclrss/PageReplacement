package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageReplacementAlgorithm {
    final private int frames;
    private int quantBitR;
    int auxQuantBitR = quantBitR;
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

    public void secondChance() {
        int pageHit = 0;
        int pageFault = 0;

        for (Page page : pagesPage) {
            if (auxQuantBitR == 0) auxQuantBitR = setBitRToZero(processMemoryPage);
            if (processMemoryPage.size() != (frames)) {
                if (containsPageID(processMemoryPage, page)) {
                    pageHit++;
                } else {
                    page.setBitR(true);
                    processMemoryPage.add(page);
                    System.out.println(processMemoryPage);
                    pageFault++;
                }
            } else {
                if (containsPageID(processMemoryPage, page)) {
                    pageHit++;
                    auxQuantBitR--;
                    page.setBitR(true);
                } else {
                    pageFault++;
                    if (!processMemoryPage.get(0).isBitR()) {
                        processMemoryPage.set(0, page);
                    } else {
                        for (Page pageR : processMemoryPage) {
                            if (!pageR.isBitR()) processMemoryPage.set(0, page);
                            else {
                                processMemoryPage.get(0).setBitR(false);
                                Collections.rotate(processMemoryPage, -1);
                            }
                        }
                    }
                    Collections.rotate(processMemoryPage, -1);
                    System.out.println(processMemoryPage);
                }
            }
        }
        System.out.println("----------- FIFO -----------");
        System.out.println("Acertos: " + pageHit);
        System.out.println("Faltas: " + pageFault);
        System.out.println("----------- Processos na memória -----------");
        processMemoryPage.forEach(System.out::println);
    }

    public void leastRecentlyUsed() {
        int pageHit = 0;
        int pageFault = 0;
        for (String s : pagesString) {
            if (processMemoryString.size() != (frames)) {
                if (processMemoryString.contains(s)) {
                    pageHit++;
                    processMemoryString.forEach(page -> {
                        while (processMemoryString.indexOf(s) != processMemoryString.size() - 1){
                            Collections.rotate(processMemoryString, -1);
                            if(processMemoryString.indexOf(s) == processMemoryString.size() - 1) break;
                        }
                    });

                } else {
                    processMemoryString.add(s);
                    System.out.println(processMemoryString);
                    pageFault++;
                }
            } else {
                if (processMemoryString.contains(s)) {
                    processMemoryString.forEach(page -> {
                        while (processMemoryString.indexOf(s) != frames){
                            Collections.rotate(processMemoryString, -1);
                            if(processMemoryString.indexOf(s) == frames) break;
                        }
                    });
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



    boolean containsPageID (List<Page> page, Page pageID) {
        return page.stream().anyMatch(p -> p.getPageId().equals(pageID.pageId));
    }

    int setBitRToZero(List<Page> processMemory){
        processMemory.forEach(page -> page.setBitR(false));
        return quantBitR;
    }



}
