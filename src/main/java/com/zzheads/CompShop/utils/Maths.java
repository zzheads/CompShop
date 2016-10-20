package com.zzheads.CompShop.utils;

import com.zzheads.CompShop.model.Product;
import com.zzheads.CompShop.utils.PackBoxes.BinPacking3D_LAFF;
import com.zzheads.CompShop.utils.PackBoxes.Box;

import java.util.ArrayList;
import java.util.List;

public class Maths {
    public static double max (double[] values) {
        if (values == null) return Double.NaN;
        if (values.length == 0) return 0.0;
        double max = values[0];
        for (int i=1;i<values.length;i++) {
            if (values[i] > max)
                max = values[i];
        }
        return max;
    }

    public static double min (double[] values) {
        if (values == null) return Double.NaN;
        if (values.length == 0) return 0.0;
        double min = values[0];
        for (int i=1;i<values.length;i++) {
            if (values[i] < min)
                min = values[i];
        }
        return min;
    }

    private static List<Box> getBoxes (List<Product> products) {
        List<Box> boxes = new ArrayList<>();
        for (Product product : products) {
            boxes.add(new Box((int) product.getHeightInCm(), (int) product.getLengthInCm(), (int) product.getWidthInCm()));
        }
        return boxes;
    }

    public static double[] dimensionsPack (List<Product> products) {
        // dimensions in cm, pack by LAFF algorithm
        BinPacking3D_LAFF pack = new BinPacking3D_LAFF(getBoxes(products));
        return new double[] {
                pack.containerDimension.get("height"),
                pack.containerDimension.get("length"),
                pack.containerDimension.get("breadth")
        };
    }

    public static double volumePack (List<Product> products) {
        BinPacking3D_LAFF pack = new BinPacking3D_LAFF(getBoxes(products));
        return ((double) pack.containerDimension.get("height") * pack.containerDimension.get("length") * pack.containerDimension.get("breadth"));
    }
}
