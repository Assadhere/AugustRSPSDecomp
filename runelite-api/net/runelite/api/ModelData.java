package net.runelite.api;

public interface ModelData extends Mesh<ModelData>, Renderable {
   int DEFAULT_AMBIENT = 64;
   int DEFAULT_CONTRAST = 768;
   int DEFAULT_X = -50;
   int DEFAULT_Y = -10;
   int DEFAULT_Z = -50;

   short[] getFaceColors();

   Model light(int var1, int var2, int var3, int var4, int var5);

   Model light();

   ModelData recolor(short var1, short var2);

   ModelData retexture(short var1, short var2);

   ModelData shallowCopy();

   ModelData cloneVertices();

   ModelData cloneColors();

   ModelData cloneTextures();

   ModelData cloneTransparencies();

   ModelData cloneTransparencies(boolean var1);
}
