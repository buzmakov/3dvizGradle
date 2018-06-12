package com.rbtm.reconstruction.MarchingCubes;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class MarchingCubes {
    static float[] lerp(float[] vec1, float[] vec2, float alpha){
        return new float[]{vec1[0] + (vec2[0] - vec1[0]) * alpha, vec1[1] + (vec2[1] - vec1[1]) * alpha, vec1[2] + (vec2[2] - vec1[2]) * alpha};
    }

    static public ArrayList<float[]> marchingCubesMat(List<Mat> m, float[] voxDim, float isoLevel, int cubeSize) {
        int xDim = m.get(0).cols();
        int yDim = m.get(0).rows();
        int zDim = m.size();
        System.out.printf("x=%d; y=%d; z=%d\n", xDim, yDim, zDim);

        ArrayList<float[]> vertices = new ArrayList<>();
        // Actual position along edge weighted according to function values.
        float vertList[][] = new float[12][3];


        // Calculate maximal possible axis value (used in vertice normalization)
        float maxX = voxDim[0] * (xDim - 1);
        float maxY = voxDim[1] * (yDim - 1);
        float maxZ = voxDim[2] * (zDim - 1);
        float maxAxisVal = Math.max(maxX, Math.max(maxY, maxZ));

        // Volume iteration
        for (int z = 0; z < zDim - cubeSize; z+=cubeSize) {
            System.out.println(z);
            for (int y = 0; y < yDim - cubeSize; y+=cubeSize) {
                for (int x = 0; x < xDim - cubeSize; x+=cubeSize) {

                    // Indices pointing to cube vertices
                    //              pyz  ___________________  pxyz
                    //                  /|                 /|
                    //                 / |                / |
                    //                /  |               /  |
                    //          pz   /___|______________/pxz|
                    //              |    |              |   |
                    //              |    |              |   |
                    //              | py |______________|___| pxy
                    //              |   /               |   /
                    //              |  /                |  /
                    //              | /                 | /
                    //              |/__________________|/
                    //             p                     px

                    //							  X              Y                    Z
                    float position[] = new float[]{x * voxDim[0], y * voxDim[1], z * voxDim[2]};

                    // Voxel intensities
                    double value0 = m.get(z).get(y, x)[0],
                            value1 = m.get(z).get(y, x + cubeSize)[0],
                            value2 = m.get(z).get(y + cubeSize, x)[0],
                            value3 = m.get(z).get(y + cubeSize, x + cubeSize)[0],
                            value4 = m.get(z + cubeSize).get(y, x)[0],
                            value5 = m.get(z + cubeSize).get(y, x + cubeSize)[0],
                            value6 = m.get(z + cubeSize).get(y + cubeSize, x)[0],
                            value7 = m.get(z + cubeSize).get(y + cubeSize, x + cubeSize)[0];

                    // Voxel is active if its intensity is above isolevel
                    int cubeindex = 0;
                    if (value0 > isoLevel) cubeindex |= 1;
                    if (value1 > isoLevel) cubeindex |= 2;
                    if (value2 > isoLevel) cubeindex |= 8;
                    if (value3 > isoLevel) cubeindex |= 4;
                    if (value4 > isoLevel) cubeindex |= 16;
                    if (value5 > isoLevel) cubeindex |= 32;
                    if (value6 > isoLevel) cubeindex |= 128;
                    if (value7 > isoLevel) cubeindex |= 64;

                    // Fetch the triggered edges
                    int bits = TablesMC.MC_EDGE_TABLE[cubeindex];

                    // If no edge is triggered... skip
                    if (bits == 0) continue;

                    // Interpolate the positions based od voxel intensities
                    float mu = 0.5f;

                    // bottom of the cube
                    if ((bits & 1) != 0) {
                        mu = (float) ((isoLevel - value0) / (value1 - value0));
                        vertList[0] = lerp(position, new float[]{position[0] + voxDim[0], position[1], position[2]}, mu);
                    }
                    if ((bits & 2) != 0) {
                        mu = (float) ((isoLevel - value1) / (value3 - value1));
                        vertList[1] = lerp(new float[]{position[0] + voxDim[0], position[1], position[2]}, new float[]{position[0] + voxDim[0], position[1] + voxDim[1], position[2]}, mu);
                    }
                    if ((bits & 4) != 0) {
                        mu = (float) ((isoLevel - value2) / (value3 - value2));
                        vertList[2] = lerp(new float[]{position[0], position[1] + voxDim[1], position[2]}, new float[]{position[0] + voxDim[0], position[1] + voxDim[1], position[2]}, mu);
                    }
                    if ((bits & 8) != 0) {
                        mu = (float) ((isoLevel - value0) / (value2 - value0));
                        vertList[3] = lerp(position, new float[]{position[0], position[1] + voxDim[1], position[2]}, mu);
                    }
                    // top of the cube
                    if ((bits & 16) != 0) {
                        mu = (float) ((isoLevel - value4) / (value5 - value4));
                        vertList[4] = lerp(new float[]{position[0], position[1], position[2] + voxDim[2]}, new float[]{position[0] + voxDim[0], position[1], position[2] + voxDim[2]}, mu);
                    }
                    if ((bits & 32) != 0) {
                        mu = (float) ((isoLevel - value5) / (value7 - value5));
                        vertList[5] = lerp(new float[]{position[0] + voxDim[0], position[1], position[2] + voxDim[2]}, new float[]{position[0] + voxDim[0], position[1] + voxDim[1], position[2] + voxDim[2]}, mu);
                    }
                    if ((bits & 64) != 0) {
                        mu = (float) ((isoLevel - value6) / (value7 - value6));
                        vertList[6] = lerp(new float[]{position[0], position[1] + voxDim[1], position[2] + voxDim[2]}, new float[]{position[0] + voxDim[0], position[1] + voxDim[1], position[2] + voxDim[2]}, mu);
                    }
                    if ((bits & 128) != 0) {
                        mu = (float) ((isoLevel - value4) / (value6 - value4));
                        vertList[7] = lerp(new float[]{position[0], position[1], position[2] + voxDim[2]}, new float[]{position[0], position[1] + voxDim[1], position[2] + voxDim[2]}, mu);
                    }
                    // vertical lines of the cube
                    if ((bits & 256) != 0) {
                        mu = (float) ((isoLevel - value0) / (value4 - value0));
                        vertList[8] = lerp(position, new float[]{position[0], position[1], position[2] + voxDim[2]}, mu);
                    }
                    if ((bits & 512) != 0) {
                        mu = (float) ((isoLevel - value1) / (value5 - value1));
                        vertList[9] = lerp(new float[]{position[0] + voxDim[0], position[1], position[2]}, new float[]{position[0] + voxDim[0], position[1], position[2] + voxDim[2]}, mu);
                    }
                    if ((bits & 1024) != 0) {
                        mu = (float) ((isoLevel - value3) / (value7 - value3));
                        vertList[10] = lerp(new float[]{position[0] + voxDim[0], position[1] + voxDim[1], position[2]}, new float[]{position[0] + voxDim[0], position[1]+ voxDim[1], position[2] + voxDim[2]}, mu);
                    }
                    if ((bits & 2048) != 0) {
                        mu = (float) ((isoLevel - value2) / (value6 - value2));
                        vertList[11] = lerp(new float[]{position[0], position[1] + voxDim[1], position[2]}, new float[]{position[0], position[1] + voxDim[1], position[2] + voxDim[2]}, mu);
                    }

                    // construct triangles -- get correct vertices from triTable.
                    int i = 0;
                    // "Re-purpose cubeindex into an offset into triTable."
                    cubeindex <<= 4;

                    while (TablesMC.MC_TRI_TABLE[cubeindex + i] != -1) {
                        int index1 = TablesMC.MC_TRI_TABLE[cubeindex + i];
                        int index2 = TablesMC.MC_TRI_TABLE[cubeindex + i + 1];
                        int index3 = TablesMC.MC_TRI_TABLE[cubeindex + i + 2];

                        // Add triangles vertices normalized with the maximal possible value
                        vertices.add(new float[] {vertList[index3][0] / maxAxisVal - 0.5f, vertList[index3][1] / maxAxisVal - 0.5f, vertList[index3][2] / maxAxisVal - 0.5f});
                        vertices.add(new float[] {vertList[index2][0] / maxAxisVal - 0.5f, vertList[index2][1] / maxAxisVal - 0.5f, vertList[index2][2] / maxAxisVal - 0.5f});
                        vertices.add(new float[] {vertList[index1][0] / maxAxisVal - 0.5f, vertList[index1][1] / maxAxisVal - 0.5f, vertList[index1][2] / maxAxisVal - 0.5f});

                        i += 3;
                    }
                }
            }
        }

        return vertices;
    }
}
