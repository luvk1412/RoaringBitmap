package org.roaringbitmap.experiments;

import org.roaringbitmap.RoaringBitmap;

import it.uniroma3.mat.extendedset.intset.ConciseSet;

/**
 * The purpose of this case is to identify cases where
 * roaring fares poorly as far as compression goes.
 * 
 * @author Daniel Lemire
 *
 */
public class BadCompressionHunter {

        /**
         * @param args
         */
        public static void main(String[] args) {
                RealDataRetriever dataSrc = new RealDataRetriever(args[0]);
                String dataset = args[1];
                int NTRIALS = Integer.parseInt(args[2]);
                System.out.println(NTRIALS + " tests on " + dataset);
                double worse = 1;
                for (int i = 0; i < 2*NTRIALS; ++i) {
                        int[] data = dataSrc.fetchBitPositions(dataset, i);
                        if (data.length < 1024)
                                continue;
                        double density = data.length * 1.0 / data[data.length - 1];
                        if(density * 1024 < 1) continue;
                        ConciseSet ans = new ConciseSet();
                        for (int j : data)
                                ans.add(j);
                        double concisesize = ans.collectionCompressionRatio() * 4;
                        RoaringBitmap rr = RoaringBitmap.bitmapOf(data);
                        double rrsize = rr.getSizeInBytes();
                        if(rrsize / concisesize < worse) {
                                System.out.println("concisesize = "+concisesize);
                                System.out.println("rrsize = "+rrsize);
                                worse = rrsize / concisesize;
                                System.out.println("ratio = "+worse);
                                System.out.println("density = "+density);
                        }


                }

                
        }
}
