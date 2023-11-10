package logic;

import java.util.ArrayList;
import java.util.Collections;

public class PageReplacementAlgorithm {
    private int frames;
    private int quantBitR;
    ArrayList<Page> pages;
    ArrayList<Page> processMemory;
    public PageReplacementAlgorithm(int frames, int quantBitR) {
        this.frames = frames;
        this.quantBitR = quantBitR;
        pages = SetPageList.pages;
        processMemory = new ArrayList<>(frames);
    }

    public void fifo() {
        int pageHit = 0;
        int pageFault = 0;
        for (int i = 0; i < pages.size(); i++){
            if(processMemory.contains(null)){
                processMemory.add(pages.get(i));
            }else{
                if(processMemory.contains(pages.get(i))) {
                    pageHit++;
                    //preciso comparar a string pageID e não o objeto pq pode dar erro

                }
                else{
                    pageFault++;
                    Collections.rotate(processMemory, 1);
                    processMemory.set(frames - 1, pages.get(i));
                    //processMemory.forEach(page -> page.pageId == pages.get(i)); testar comparação de atributo da classe

                }
            }
        }
        System.out.println("-----------FIFO-----------");
        System.out.println("Acertos: " + pageHit);
        System.out.println("Faltas: " + pageFault);
    }
}
