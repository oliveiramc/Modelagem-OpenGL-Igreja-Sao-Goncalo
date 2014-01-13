package utils;

import javax.media.opengl.GL2;

public class GeometryUtils {

    public static void main(String[] args) {

        for (float x = -0.5f; x >= -4.5f; x -= 0.1f) {
            float a, b, c, //coeficientes
                    delta, //delta
                    sqrtdelta, //raiz quadrada de delta
                    raiz1, raiz2;  //raízes

            //Passo 1: Recebendo os coeficientes
            a = 1;
            b = -16;
            c = (float) (66.25f+Math.sqrt(x)+(5.0f*x));

            //Passo 2: Checando se a equação é válida
            if (a != 0) {

                //Passo 3: recebendo o valor de delta e calculando sua raiz quadrada
                delta = (b * b) - (4 * a * c);
                sqrtdelta = (float) Math.sqrt(delta);

                //Passo 4: se a raiz de delta for maior que 0, as raízes são reais    
                if (delta >= 0) {
                    raiz1 = ((-1) * b + sqrtdelta) / (2 * a);
                    raiz2 = ((-1) * b - sqrtdelta) / (2 * a);
                    System.out.printf("Raízes: %.2f e %.2f", raiz1, raiz2);
                } //Passo 5: se delta for menor que 0, as raízes serão complexas
                else {
                    delta = -delta;
                    sqrtdelta = (float) Math.sqrt(delta);
                    System.out.printf("Raíz 1: %.2f + i.%.2f\n", (-b) / (2 * a), (sqrtdelta) / (2 * a));
                    System.out.printf("Raíz 2: %.2f - i.%.2f\n", (-b) / (2 * a), (sqrtdelta) / (2 * a));

                }

            } else {
                System.out.println("Coeficiente 'a' inválido. Não é uma equação do 2o grau");
            }
        }
    }

    public static double[] calculateNormal(double[] p1, double[] p2, double[] p3) {
        double d = p2[0] - p1[0];
        double e = p2[1] - p1[1];
        double f = p2[2] - p1[2];

        double a = p3[0] - p1[0];
        double b = p3[1] - p1[1];
        double c = p3[2] - p1[2];

        double[] normal = {(b * f - c * e), (c * d - f * a), (a * e - d * b)};// 3

        double lenght = Math.pow(
                (normal[0] * normal[0] + normal[1] * normal[1] + normal[2]
                * normal[2]), 0.5);

        normal[0] = normal[0] / lenght;
        normal[1] = normal[1] / lenght;
        normal[2] = normal[2] / lenght;// Math.abs(normal[2] / lenght);

        return normal;
    }

    public static double[][] createPortalVertices(float x_init, float z_init,
            float arc_width, float arc_height) {
        double[][] arr = new double[14][3];

        // float x_width = 1.5f;
        double arc_center = x_init + arc_width / 2f;
        // double z_height = 2d;
        // double z_init = 1.5;
        arr[0] = new double[]{x_init, 0.0, z_init};
        arr[1] = new double[]{x_init, 0.0, z_init + arc_height};

        // Semi circulo
        int cur_angle = 180;
        int num_vert = 10;
        float angleSum = cur_angle / num_vert;

        for (int i = 2; i < arr.length - 2; i++) {
            double x_pos = arc_center + arc_width / 2f
                    * Math.cos(Math.toRadians(cur_angle));
            double z_pos = z_init + arc_height + arc_width / 2f
                    * Math.sin(Math.toRadians(cur_angle));

            arr[i] = new double[]{x_pos, 0.0, z_pos};
            cur_angle -= angleSum;
        }

        arr[12] = new double[]{x_init + arc_width, 0.0, z_init + arc_height};
        arr[13] = new double[]{x_init + arc_width, 0.0, z_init};

        return arr;
    }

    public static void calculatePortalInside(GL2 gl, double[][] points,
            double y_translate, boolean drawfloor) {
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int j = 0; j < points.length; j++) {
            double[] vertice1 = new double[]{points[j][0], points[j][1],
                points[j][2]};
            double[] vertice2 = new double[]{points[j][0], y_translate,
                points[j][2]};
            if (j > 0) {
                double[] vertice3 = new double[]{points[j - 1][0],
                    points[j - 1][1], points[j - 1][2]};
                gl.glNormal3dv(GeometryUtils.calculateNormal(vertice1,
                        vertice2, vertice3), 0);
            }
            if (j % 2 == 0) {
                gl.glTexCoord2d(0, 0);
            } else {
                gl.glTexCoord2d(0.2, 0);
            }

            gl.glVertex3dv(vertice1, 0);

            if (j % 2 == 0) {
                gl.glTexCoord2d(0.2, 0.2);
            } else {
                gl.glTexCoord2d(0, 0.2);
            }
            gl.glVertex3dv(vertice2, 0);
        }

        if (drawfloor) {
            double[] vertice1 = new double[]{points[0][0], points[0][1],
                points[0][2]};
            double[] vertice2 = new double[]{points[0][0], y_translate,
                points[0][2]};

            double[] vertice3 = new double[]{points[points.length - 1][0],
                points[points.length - 1][1], points[points.length - 1][2]};
            gl.glNormal3dv(
                    GeometryUtils.calculateNormal(vertice1, vertice2, vertice3),
                    0);

            gl.glTexCoord2d(0, 0);
            gl.glVertex3dv(vertice1, 0);

            gl.glTexCoord2d(0.3, 0.3);
            gl.glVertex3dv(vertice2, 0);
        }

        gl.glEnd();

    }

}
