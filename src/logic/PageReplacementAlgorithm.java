package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PageReplacementAlgorithm {
    final private int frames;
    private int quantBitR;
    int auxQuantBitR = quantBitR;
    //Page pageToBeReplaced;
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
        Page pageToBeReplaced;
        ArrayList<Page> auxProcessMemory = new ArrayList<>();
        for (Page page : pagesPage) {
            page.setBitR(true);
            if (auxQuantBitR == 0) auxQuantBitR = setBitRToZero(processMemoryPage);
            if (processMemoryPage.size() != (frames)) {
                if (containsPageID(processMemoryPage, page)) {
                    //auxQuantBitR--;
                    //auxProcessMemory.get(indexOfPageById(auxProcessMemory, page)).setBitR(true);
                    processMemoryPage.get(indexOfPageById(processMemoryPage, page)).setBitR(true);
                    pageHit++;
                } else {
                    //page.setBitR(true);
                    processMemoryPage.add(page);
                    System.out.println(processMemoryPage);
                    pageFault++;
                }
                if(processMemoryPage.size() == frames) auxProcessMemory.addAll(processMemoryPage);
            } else {
                if (containsPageID(processMemoryPage, page)) {
                    auxProcessMemory.get(indexOfPageById(auxProcessMemory, page)).setBitR(true);
                    processMemoryPage.get(indexOfPageById(processMemoryPage, page)).setBitR(true);
                    pageHit++;
                } else {
                    pageFault++;
                    if (!auxProcessMemory.get(0).isBitR()) {
                        pageToBeReplaced = auxProcessMemory.get(0);
                        auxProcessMemory.set(0,page);
                        processMemoryPage.set(indexOfPageById(processMemoryPage, pageToBeReplaced), page);
                        Collections.rotate(auxProcessMemory, -1);
                    } else {
                        while (true) {
                            if (!auxProcessMemory.get(0).isBitR()) {
                                pageToBeReplaced = auxProcessMemory.get(0);
                                auxProcessMemory.set(0,page);
                                processMemoryPage.set(indexOfPageById(processMemoryPage, pageToBeReplaced), page);
                                Collections.rotate(auxProcessMemory, -1);
                                if (auxProcessMemory.get(frames - 1) == page) break;
                            } else {
                                auxProcessMemory.get(0).setBitR(false);
                                Collections.rotate(auxProcessMemory, -1);
                            }
                        }
                    }
                    System.out.println(processMemoryPage);
                }
            }
            auxQuantBitR--;
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
        Page pageToBeReplaced;
        ArrayList<Page> auxProcessMemory = new ArrayList<>();
        auxQuantBitR = quantBitR;
        for (Page page : pagesPage) {
            //auxQuantBitR--;
            page.setBitR(true);
            if (auxQuantBitR == 0) auxQuantBitR = setBitRToZero(auxProcessMemory);
            if (processMemoryPage.size() != (frames)) {
                //auxQuantBitR--;
                if (containsPageID(processMemoryPage, page)) {
                    page.setBitR(true);
                    if (page.type.equals("W")) {
                        page.setBitM(true);
                        //auxProcessMemory.set(indexOfPageById(auxProcessMemory,page), page);
                        processMemoryPage.set(indexOfPageById(processMemoryPage, page), page);
                    } else {
                        processMemoryPage.get(indexOfPageById(processMemoryPage, page)).setBitR(true);
                    }
                    //auxQuantBitR--;
                    pageHit++;
                } else {
                    page.setBitR(true);
                    if (page.type.equals("W")) page.setBitM(true);
                    processMemoryPage.add(page);
                    //auxProcessMemory.add(page);
                    System.out.println(processMemoryPage);
                    //auxQuantBitR--;
                    pageFault++;
                }
                if (processMemoryPage.size() == frames) auxProcessMemory.addAll(processMemoryPage);
            } else {
                //auxQuantBitR--;
                if (containsPageID(processMemoryPage, page)) {
                    pageHit++;
                    //auxQuantBitR--;
                    page.setBitR(true);
                    if (page.type.equals("W")) {
                        page.setBitM(true);
                        processMemoryPage.set(indexOfPageById(processMemoryPage, page), page);
                        auxProcessMemory.set(indexOfPageById(auxProcessMemory, page), page);
                    } else {
                        auxProcessMemory.get(indexOfPageById(auxProcessMemory, page)).setBitR(true);
                        processMemoryPage.get(indexOfPageById(processMemoryPage, page)).setBitR(true);
                    }
                } else {
                    pageFault++;
                    boolean replaced = false;
                    if (isThereAnyCase(processMemoryPage, false, false)) {
                        for (int p = 0; p < processMemoryPage.size(); p++) {
                            if (!auxProcessMemory.get(p).isBitR() && !auxProcessMemory.get(p).isBitM()) {
                                if (page.type.equals("W")) page.setBitM(true);
                                page.setBitR(true);
                                pageToBeReplaced = auxProcessMemory.get(p);
                                auxProcessMemory.set(p, page);
                                processMemoryPage.set(indexOfPageById(processMemoryPage, pageToBeReplaced), page);
                                replaced = true;
                                //tenho q setar a página que entrou no aux no lugar da qual deve entrar na memoria
                                while (auxProcessMemory.indexOf(page) != frames - 1) {
                                    Collections.rotate(auxProcessMemory
                                            .subList(auxProcessMemory.indexOf(page), auxProcessMemory.size()), -1);
                                    System.out.println(auxProcessMemory);
                                }
                                //Collections.rotate(auxProcessMemory, -1);
                                // auxQuantBitR--;
                                break;
                            }
                        }

                    }
                    if (isThereAnyCase(processMemoryPage, false, true)) {
                        for (int p = 0; p < processMemoryPage.size(); p++) {
                            if (replaced) break;
                            if (!auxProcessMemory.get(p).isBitR() && auxProcessMemory.get(p).isBitM()) {
                                if (page.type.equals("W")) page.setBitM(true);
                                page.setBitR(true);
                                pageToBeReplaced = auxProcessMemory.get(p);
                                auxProcessMemory.set(p, page);
                                processMemoryPage.set(indexOfPageById(processMemoryPage, pageToBeReplaced), page);
                                replaced = true;
                                while (auxProcessMemory.indexOf(page) != frames - 1) {
                                    Collections.rotate(auxProcessMemory
                                            .subList(auxProcessMemory.indexOf(page), auxProcessMemory.size()), -1);
                                    System.out.println(auxProcessMemory);
                                }
                                //Collections.rotate(auxProcessMemory, -1);
                                //auxQuantBitR--;
                                break;
                            }
                        }
                    }
                    if (isThereAnyCase(processMemoryPage, true, false)) {
                        for (int p = 0; p < processMemoryPage.size(); p++) {
                            if (replaced) break;
                            if (auxProcessMemory.get(p).isBitR() && !auxProcessMemory.get(p).isBitM()) {
                                if (page.type.equals("W")) page.setBitM(true);
                                page.setBitR(true);
                                pageToBeReplaced = auxProcessMemory.get(p);
                                auxProcessMemory.set(p, page);
                                processMemoryPage.set(indexOfPageById(processMemoryPage, pageToBeReplaced), page);
                                replaced = true;
                                while (auxProcessMemory.indexOf(page) != frames - 1) {
                                    Collections.rotate(auxProcessMemory
                                            .subList(auxProcessMemory.indexOf(page), auxProcessMemory.size()), -1);
                                    System.out.println(auxProcessMemory);
                                }
                                //Collections.rotate(auxProcessMemory, -1);
                                //auxQuantBitR--;
                                break;
                            }
                        }
                    }
                    if (isThereAnyCase(processMemoryPage, true, true)) {
                        for (int p = 0; p < processMemoryPage.size(); p++) {
                            if (replaced) break;
                            if (auxProcessMemory.get(p).isBitR() && auxProcessMemory.get(p).isBitM()) {
                                if (page.type.equals("W")) page.setBitM(true);
                                page.setBitR(true);
                                pageToBeReplaced = auxProcessMemory.get(p);
                                auxProcessMemory.set(p, page);
                                processMemoryPage.set(indexOfPageById(processMemoryPage, pageToBeReplaced), page);
                                replaced = true;
                                while (auxProcessMemory.indexOf(page) != frames - 1) {
                                    Collections.rotate(auxProcessMemory
                                            .subList(auxProcessMemory.indexOf(page), auxProcessMemory.size()), -1);
                                    System.out.println(auxProcessMemory);
                                }
                                //Collections.rotate(auxProcessMemory, -1);
                                //auxQuantBitR--;
                                break;
                            }
                        }
                    }
                    System.out.println(processMemoryPage);
                }
            }
            auxQuantBitR--;
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

    int indexOfPageById(List<Page> pageList, Page pageID) {
        AtomicInteger i = new AtomicInteger();
        pageList.forEach(p -> {
            if (p.getPageId().equals(pageID.pageId)) {
                i.set(pageList.indexOf(p));
            }
        });
        return i.get();
    }

    int setBitRToZero(List<Page> processMemory) {
        int i;
        processMemory.forEach(page -> page.setBitR(false));
        return i = quantBitR;
    }

    boolean isThereAnyCase(List<Page> processMemory, boolean bitR, boolean bitM) {
        return processMemory.stream().anyMatch(p -> p.isBitR() == bitR && p.isBitM() == bitM);
    }


}
