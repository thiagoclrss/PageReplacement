package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
                    auxQuantBitR--;
                    pageHit++;
                } else {
                    page.setBitR(true);
                    processMemoryPage.add(page);
                    System.out.println(processMemoryPage);
                    auxQuantBitR--;
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
                        Collections.rotate(processMemoryPage, -1);
                        auxQuantBitR--;
                    } else {
                        while (true) {
                            //com o for o laço percorria todo o vetor e n add a pagina, preciso encontrar a logca para add a pagina na primeira posicao
                            if (!processMemoryPage.get(0).isBitR()) {
                                processMemoryPage.set(0, page);
                                processMemoryPage.get(0).setBitR(true);
                                if (processMemoryPage.get(0) == page) break;
                                Collections.rotate(processMemoryPage, -1);
                            } else {
                                processMemoryPage.get(0).setBitR(false);

                                Collections.rotate(processMemoryPage, -1);
                            }
                        }
                        auxQuantBitR--;
                    }
                    System.out.println(processMemoryPage);
                }
            }
        }
        System.out.println("----------- SEGUNDA CHANCE -----------");
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
                        while (processMemoryString.indexOf(s) != processMemoryString.size() - 1) {
                            Collections.rotate(processMemoryString.subList(processMemoryString.indexOf(s), processMemoryString.size()), -1);
                            System.out.println(processMemoryString);
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
                        while (processMemoryString.indexOf(s) != frames - 1) {
                            Collections.rotate(processMemoryString.subList(processMemoryString.indexOf(s), processMemoryString.size()), -1);
                            System.out.println(processMemoryString);

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
        System.out.println("----------- MRU -----------");
        System.out.println("Acertos: " + pageHit);
        System.out.println("Faltas: " + pageFault);
        System.out.println("----------- Processos na memória -----------");
        processMemoryString.forEach(System.out::println);
    }

    public void notRecentlyUsed() {
        int pageHit = 0;
        int pageFault = 0;
        ArrayList<Page> auxProcessMemory = new ArrayList<>();

        for (Page page : pagesPage) {
            if (auxQuantBitR == 0) auxQuantBitR = setBitRToZero(auxProcessMemory);
            if (processMemoryPage.size() != (frames)) {
                if (containsPageID(processMemoryPage, page)) {
                    if(page.type.equals("W")) {
                        page.setBitM(true);
                        processMemoryPage.set(indexOfPageById(processMemoryPage,page), page);
                        //auxProcessMemory.set(indexOfPageById(auxProcessMemory,page), page);
                    }
                    page.setBitR(true);
                    auxQuantBitR--;
                    pageHit++;
                } else {
                    page.setBitR(true);
                    if(page.type.equals("W")) page.setBitM(true);
                    processMemoryPage.add(page);
                    //auxProcessMemory.add(page);
                    System.out.println(processMemoryPage);
                    auxQuantBitR--;
                    pageFault++;
                }
                auxProcessMemory = processMemoryPage;
            } else {
                if (containsPageID(processMemoryPage, page)) {
                    pageHit++;
                    auxQuantBitR--;
                    if(page.type.equals("W")) {
                        page.setBitM(true);
                        processMemoryPage.set(indexOfPageById(processMemoryPage,page), page);
                    }
                    page.setBitR(true);
                } else {
                    pageFault++;

                        if (isThereAnyCase(processMemoryPage, false, false)) {
                            for(int p = 0; p < processMemoryPage.size(); p++){
                                if(!auxProcessMemory.get(p).isBitR() && !auxProcessMemory.get(p).isBitM()){
                                    if(page.type.equals("W")) page.setBitM(true);
                                    page.setBitR(true);
                                    auxProcessMemory.set(p, page);
                                    processMemoryPage.set(p,page);
                                    //tenho q setar a página que entrou no aux no lugar da qual deve entrar na memoria
                                    Collections.rotate(auxProcessMemory, -1);
                                    auxQuantBitR--;
                                    break;
                                }
                            }

                        }
                        if (isThereAnyCase(processMemoryPage, false, true)) {
                            for(int p = 0; p < processMemoryPage.size(); p++){
                                if(!auxProcessMemory.get(p).isBitR() && auxProcessMemory.get(p).isBitM()){
                                    if(page.type.equals("W")) page.setBitM(true);
                                    page.setBitR(true);
                                    auxProcessMemory.set(p, page);
                                    processMemoryPage.set(p,page);
                                    Collections.rotate(auxProcessMemory, -1);
                                    auxQuantBitR--;
                                    break;
                                }
                            }
                        }
                    if (isThereAnyCase(processMemoryPage, true, false)) {
                        for(int p = 0; p < processMemoryPage.size(); p++){
                            if(auxProcessMemory.get(p).isBitR() && !auxProcessMemory.get(p).isBitM()){
                                if(page.type.equals("W")) page.setBitM(true);
                                page.setBitR(true);
                                auxProcessMemory.set(p, page);
                                processMemoryPage.set(p,page);
                                Collections.rotate(auxProcessMemory, -1);
                                auxQuantBitR--;
                                break;
                            }
                        }
                    }
                    if (isThereAnyCase(processMemoryPage, true, true)) {
                        for(int p = 0; p < processMemoryPage.size(); p++){
                            if(auxProcessMemory.get(p).isBitR() && auxProcessMemory.get(p).isBitM()){
                                if(page.type.equals("W")) page.setBitM(true);
                                page.setBitR(true);
                                auxProcessMemory.set(p, page);
                                processMemoryPage.set(p,page);
                                Collections.rotate(auxProcessMemory, -1);
                                auxQuantBitR--;
                                break;
                            }
                        }
                    }
                    System.out.println(processMemoryPage);
                }
            }
        }
        System.out.println("----------- NUR -----------");
        System.out.println("Acertos: " + pageHit);
        System.out.println("Faltas: " + pageFault);
        System.out.println("----------- Processos na memória -----------");
        processMemoryPage.forEach(System.out::println);
    }


    boolean containsPageID(List<Page> page, Page pageID) {
        return page.stream().anyMatch(p -> p.getPageId().equals(pageID.pageId));
    }

    int indexOfPageById(List<Page> page, Page pageID){
        AtomicInteger i = new AtomicInteger();
        page.forEach(p -> {
            if(p.getPageId().equals(pageID.pageId)){
               i.set(page.indexOf(p));
            }
        });
        return i.get();
    }

    int setBitRToZero(List<Page> processMemory) {
        int i;
        processMemory.forEach(page -> page.setBitR(false));
        return i = quantBitR - 1;
    }

    boolean isThereAnyCase(List<Page> processMemory, boolean bitR, boolean bitM){
        return  processMemory.stream().anyMatch(p -> p.isBitR() == bitR && p.isBitR()== bitR);
    }


}
