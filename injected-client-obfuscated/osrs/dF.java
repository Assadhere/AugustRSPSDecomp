package osrs;

public class dF extends df {
   public dF(dN var1) {
      super(var1);
   }

   public boolean g() {
      return false;
   }

   public void b(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int var11, int var12) {
      int var13 = (int)var4;
      int var14 = (int)var5;
      int var15 = (int)var6;
      int var16 = (int)var1;
      int var17 = (int)var2;
      int var18 = (int)var3;
      int var19 = var14 - var13;
      int var20 = var17 - var16;
      int var21 = var15 - var13;
      int var22 = var18 - var16;
      int var23 = var11 - var10;
      int var24 = var12 - var10;
      int var25;
      if (var17 != var18) {
         var25 = (var15 - var14 << 14) / (var18 - var17);
      } else {
         var25 = 0;
      }

      int var26;
      if (var16 != var17) {
         var26 = (var19 << 14) / var20;
      } else {
         var26 = 0;
      }

      int var27;
      if (var16 != var18) {
         var27 = (var21 << 14) / var22;
      } else {
         var27 = 0;
      }

      int var28 = var19 * var22 - var20 * var21;
      if (var28 != 0) {
         int var29 = (var22 * var23 - var20 * var24 << 8) / var28;
         int var30 = (var19 * var24 - var21 * var23 << 8) / var28;
         int[] var31 = this.k.l;
         int var32 = this.k.g;
         int var33;
         int var34;
         int var35;
         int var36;
         int var37;
         int var38;
         int var39;
         if (var16 <= var17 && var16 <= var18) {
            if (var16 < var32) {
               if (var17 > var32) {
                  var17 = var32;
               }

               if (var18 > var32) {
                  var18 = var32;
               }

               var33 = (var10 << 8) - var13 * var29 + var29;
               if (var17 < var18) {
                  var35 = var34 = var13 << 14;
                  if (var16 < 0) {
                     var35 -= var16 * var27;
                     var34 -= var16 * var26;
                     var33 -= var16 * var30;
                     var16 = 0;
                  }

                  var36 = var14 << 14;
                  if (var17 < 0) {
                     var36 -= var17 * var25;
                     var17 = 0;
                  }

                  if (var16 != var17 && var27 < var26 || var16 == var17 && var27 > var25) {
                     var37 = var18 - var17;
                     var38 = var17 - var16;
                     var39 = var31[var16];

                     while(true) {
                        --var38;
                        if (var38 < 0) {
                           while(true) {
                              --var37;
                              if (var37 < 0) {
                                 return;
                              }

                              this.a(aU.h, var39, 0, 0, var35 >> 14, var36 >> 14, var33, var29);
                              var35 += var27;
                              var36 += var25;
                              var33 += var30;
                              var39 += aU.f;
                           }
                        }

                        this.a(aU.h, var39, 0, 0, var35 >> 14, var34 >> 14, var33, var29);
                        var35 += var27;
                        var34 += var26;
                        var33 += var30;
                        var39 += aU.f;
                     }
                  }

                  var37 = var18 - var17;
                  var38 = var17 - var16;
                  var39 = var31[var16];

                  while(true) {
                     --var38;
                     if (var38 < 0) {
                        while(true) {
                           --var37;
                           if (var37 < 0) {
                              return;
                           }

                           this.a(aU.h, var39, 0, 0, var36 >> 14, var35 >> 14, var33, var29);
                           var35 += var27;
                           var36 += var25;
                           var33 += var30;
                           var39 += aU.f;
                        }
                     }

                     this.a(aU.h, var39, 0, 0, var34 >> 14, var35 >> 14, var33, var29);
                     var35 += var27;
                     var34 += var26;
                     var33 += var30;
                     var39 += aU.f;
                  }
               }

               var35 = var34 = var13 << 14;
               if (var16 < 0) {
                  var35 -= var16 * var27;
                  var34 -= var16 * var26;
                  var33 -= var16 * var30;
                  var16 = 0;
               }

               var36 = var15 << 14;
               if (var18 < 0) {
                  var36 -= var18 * var25;
                  var18 = 0;
               }

               if ((var16 == var18 || var27 >= var26) && (var16 != var18 || var25 <= var26)) {
                  var37 = var17 - var18;
                  var38 = var18 - var16;
                  var39 = var31[var16];

                  while(true) {
                     --var38;
                     if (var38 < 0) {
                        while(true) {
                           --var37;
                           if (var37 < 0) {
                              return;
                           }

                           this.a(aU.h, var39, 0, 0, var34 >> 14, var36 >> 14, var33, var29);
                           var36 += var25;
                           var34 += var26;
                           var33 += var30;
                           var39 += aU.f;
                        }
                     }

                     this.a(aU.h, var39, 0, 0, var34 >> 14, var35 >> 14, var33, var29);
                     var35 += var27;
                     var34 += var26;
                     var33 += var30;
                     var39 += aU.f;
                  }
               }

               var37 = var17 - var18;
               var38 = var18 - var16;
               var39 = var31[var16];

               while(true) {
                  --var38;
                  if (var38 < 0) {
                     while(true) {
                        --var37;
                        if (var37 < 0) {
                           return;
                        }

                        this.a(aU.h, var39, 0, 0, var36 >> 14, var34 >> 14, var33, var29);
                        var36 += var25;
                        var34 += var26;
                        var33 += var30;
                        var39 += aU.f;
                     }
                  }

                  this.a(aU.h, var39, 0, 0, var35 >> 14, var34 >> 14, var33, var29);
                  var35 += var27;
                  var34 += var26;
                  var33 += var30;
                  var39 += aU.f;
               }
            }
         } else if (var17 <= var18) {
            if (var17 < var32) {
               if (var18 > var32) {
                  var18 = var32;
               }

               if (var16 > var32) {
                  var16 = var32;
               }

               var33 = (var11 << 8) - var14 * var29 + var29;
               if (var18 < var16) {
                  var35 = var34 = var14 << 14;
                  if (var17 < 0) {
                     var35 -= var17 * var26;
                     var34 -= var17 * var25;
                     var33 -= var17 * var30;
                     var17 = 0;
                  }

                  var36 = var15 << 14;
                  if (var18 < 0) {
                     var36 -= var18 * var27;
                     var18 = 0;
                  }

                  if (var17 != var18 && var26 < var25 || var17 == var18 && var26 > var27) {
                     var37 = var16 - var18;
                     var38 = var18 - var17;
                     var39 = var31[var17];

                     while(true) {
                        --var38;
                        if (var38 < 0) {
                           while(true) {
                              --var37;
                              if (var37 < 0) {
                                 return;
                              }

                              this.a(aU.h, var39, 0, 0, var35 >> 14, var36 >> 14, var33, var29);
                              var35 += var26;
                              var36 += var27;
                              var33 += var30;
                              var39 += aU.f;
                           }
                        }

                        this.a(aU.h, var39, 0, 0, var35 >> 14, var34 >> 14, var33, var29);
                        var35 += var26;
                        var34 += var25;
                        var33 += var30;
                        var39 += aU.f;
                     }
                  }

                  var37 = var16 - var18;
                  var38 = var18 - var17;
                  var39 = var31[var17];

                  while(true) {
                     --var38;
                     if (var38 < 0) {
                        while(true) {
                           --var37;
                           if (var37 < 0) {
                              return;
                           }

                           this.a(aU.h, var39, 0, 0, var36 >> 14, var35 >> 14, var33, var29);
                           var35 += var26;
                           var36 += var27;
                           var33 += var30;
                           var39 += aU.f;
                        }
                     }

                     this.a(aU.h, var39, 0, 0, var34 >> 14, var35 >> 14, var33, var29);
                     var35 += var26;
                     var34 += var25;
                     var33 += var30;
                     var39 += aU.f;
                  }
               }

               var35 = var34 = var14 << 14;
               if (var17 < 0) {
                  var35 -= var17 * var26;
                  var34 -= var17 * var25;
                  var33 -= var17 * var30;
                  var17 = 0;
               }

               var36 = var13 << 14;
               if (var16 < 0) {
                  var36 -= var16 * var27;
                  var16 = 0;
               }

               if (var26 < var25) {
                  var37 = var18 - var16;
                  var38 = var16 - var17;
                  var39 = var31[var17];

                  while(true) {
                     --var38;
                     if (var38 < 0) {
                        while(true) {
                           --var37;
                           if (var37 < 0) {
                              return;
                           }

                           this.a(aU.h, var39, 0, 0, var36 >> 14, var34 >> 14, var33, var29);
                           var36 += var27;
                           var34 += var25;
                           var33 += var30;
                           var39 += aU.f;
                        }
                     }

                     this.a(aU.h, var39, 0, 0, var35 >> 14, var34 >> 14, var33, var29);
                     var35 += var26;
                     var34 += var25;
                     var33 += var30;
                     var39 += aU.f;
                  }
               }

               var37 = var18 - var16;
               var38 = var16 - var17;
               var39 = var31[var17];

               while(true) {
                  --var38;
                  if (var38 < 0) {
                     while(true) {
                        --var37;
                        if (var37 < 0) {
                           return;
                        }

                        this.a(aU.h, var39, 0, 0, var34 >> 14, var36 >> 14, var33, var29);
                        var36 += var27;
                        var34 += var25;
                        var33 += var30;
                        var39 += aU.f;
                     }
                  }

                  this.a(aU.h, var39, 0, 0, var34 >> 14, var35 >> 14, var33, var29);
                  var35 += var26;
                  var34 += var25;
                  var33 += var30;
                  var39 += aU.f;
               }
            }
         } else if (var18 < var32) {
            if (var16 > var32) {
               var16 = var32;
            }

            if (var17 > var32) {
               var17 = var32;
            }

            var33 = (var12 << 8) - var15 * var29 + var29;
            if (var16 < var17) {
               var35 = var34 = var15 << 14;
               if (var18 < 0) {
                  var35 -= var18 * var25;
                  var34 -= var18 * var27;
                  var33 -= var18 * var30;
                  var18 = 0;
               }

               var36 = var13 << 14;
               if (var16 < 0) {
                  var36 -= var16 * var26;
                  var16 = 0;
               }

               if (var25 < var27) {
                  var37 = var17 - var16;
                  var38 = var16 - var18;
                  var39 = var31[var18];

                  while(true) {
                     --var38;
                     if (var38 < 0) {
                        while(true) {
                           --var37;
                           if (var37 < 0) {
                              return;
                           }

                           this.a(aU.h, var39, 0, 0, var35 >> 14, var36 >> 14, var33, var29);
                           var35 += var25;
                           var36 += var26;
                           var33 += var30;
                           var39 += aU.f;
                        }
                     }

                     this.a(aU.h, var39, 0, 0, var35 >> 14, var34 >> 14, var33, var29);
                     var35 += var25;
                     var34 += var27;
                     var33 += var30;
                     var39 += aU.f;
                  }
               }

               var37 = var17 - var16;
               var38 = var16 - var18;
               var39 = var31[var18];

               while(true) {
                  --var38;
                  if (var38 < 0) {
                     while(true) {
                        --var37;
                        if (var37 < 0) {
                           return;
                        }

                        this.a(aU.h, var39, 0, 0, var36 >> 14, var35 >> 14, var33, var29);
                        var35 += var25;
                        var36 += var26;
                        var33 += var30;
                        var39 += aU.f;
                     }
                  }

                  this.a(aU.h, var39, 0, 0, var34 >> 14, var35 >> 14, var33, var29);
                  var35 += var25;
                  var34 += var27;
                  var33 += var30;
                  var39 += aU.f;
               }
            }

            var35 = var34 = var15 << 14;
            if (var18 < 0) {
               var35 -= var18 * var25;
               var34 -= var18 * var27;
               var33 -= var18 * var30;
               var18 = 0;
            }

            var36 = var14 << 14;
            if (var17 < 0) {
               var36 -= var17 * var26;
               var17 = 0;
            }

            if (var25 < var27) {
               var37 = var16 - var17;
               var38 = var17 - var18;
               var39 = var31[var18];

               while(true) {
                  --var38;
                  if (var38 < 0) {
                     while(true) {
                        --var37;
                        if (var37 < 0) {
                           return;
                        }

                        this.a(aU.h, var39, 0, 0, var36 >> 14, var34 >> 14, var33, var29);
                        var36 += var26;
                        var34 += var27;
                        var33 += var30;
                        var39 += aU.f;
                     }
                  }

                  this.a(aU.h, var39, 0, 0, var35 >> 14, var34 >> 14, var33, var29);
                  var35 += var25;
                  var34 += var27;
                  var33 += var30;
                  var39 += aU.f;
               }
            }

            var37 = var16 - var17;
            var38 = var17 - var18;
            var39 = var31[var18];

            while(true) {
               --var38;
               if (var38 < 0) {
                  while(true) {
                     --var37;
                     if (var37 < 0) {
                        return;
                     }

                     this.a(aU.h, var39, 0, 0, var34 >> 14, var36 >> 14, var33, var29);
                     var36 += var26;
                     var34 += var27;
                     var33 += var30;
                     var39 += aU.f;
                  }
               }

               this.a(aU.h, var39, 0, 0, var34 >> 14, var35 >> 14, var33, var29);
               var35 += var25;
               var34 += var27;
               var33 += var30;
               var39 += aU.f;
            }
         }
      }

   }

   public void a(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      if (this.k.q) {
         if (var6 > this.k.f) {
            var6 = this.k.f;
         }

         if (var5 < 0) {
            var5 = 0;
         }
      }

      if (var5 < var6) {
         int var9 = var2 + var5;
         int var10 = var5 * var8 + var7;
         int var11;
         int var12;
         int var13;
         int var14;
         int var15;
         int var16;
         int var17;
         if (this.k.r) {
            var11 = var6 - var5 >> 2;
            var12 = var8 << 2;
            if (this.k.a == 0) {
               if (var11 > 0) {
                  do {
                     var13 = (var10 & ~(var10 >> 31)) >> 8;
                     var14 = this.j[var13];
                     var10 += var12;
                     Client.a(var1, var9++, var14, 255 - super.k.a);
                     Client.a(var1, var9++, var14, 255 - super.k.a);
                     Client.a(var1, var9++, var14, 255 - super.k.a);
                     Client.a(var1, var9++, var14, 255 - super.k.a);
                     --var11;
                  } while(var11 > 0);
               }

               var13 = var6 - var5 & 3;
               if (var13 > 0) {
                  var14 = (var10 & ~(var10 >> 31)) >> 8;
                  var15 = this.j[var14];

                  do {
                     Client.a(var1, var9++, var15, 255 - super.k.a);
                     --var13;
                  } while(var13 > 0);
               }
            } else {
               var13 = this.k.a;
               var14 = 256 - this.k.a;
               int var18;
               int var19;
               if (var11 > 0) {
                  do {
                     var15 = (var10 & ~(var10 >> 31)) >> 8;
                     var16 = this.j[var15];
                     var10 += var12;
                     var17 = ((var16 & '\uff00') * var14 >> 8 & '\uff00') + ((var16 & 16711935) * var14 >> 8 & 16711935);
                     var18 = var1[var9];
                     Client.a(var1, var9++, ((var18 & '\uff00') * var13 >> 8 & '\uff00') + ((var18 & 16711935) * var13 >> 8 & 16711935) + var17, 255 - super.k.a);
                     var19 = var1[var9];
                     Client.a(var1, var9++, ((var19 & '\uff00') * var13 >> 8 & '\uff00') + ((var19 & 16711935) * var13 >> 8 & 16711935) + var17, 255 - super.k.a);
                     int var20 = var1[var9];
                     Client.a(var1, var9++, ((var20 & '\uff00') * var13 >> 8 & '\uff00') + ((var20 & 16711935) * var13 >> 8 & 16711935) + var17, 255 - super.k.a);
                     int var21 = var1[var9];
                     Client.a(var1, var9++, ((var21 & '\uff00') * var13 >> 8 & '\uff00') + ((var21 & 16711935) * var13 >> 8 & 16711935) + var17, 255 - super.k.a);
                     --var11;
                  } while(var11 > 0);
               }

               var15 = var6 - var5 & 3;
               if (var15 > 0) {
                  var16 = (var10 & ~(var10 >> 31)) >> 8;
                  var17 = this.j[var16];
                  var18 = ((var17 & '\uff00') * var14 >> 8 & '\uff00') + ((var17 & 16711935) * var14 >> 8 & 16711935);

                  do {
                     var19 = var1[var9];
                     Client.a(var1, var9++, ((var19 & '\uff00') * var13 >> 8 & '\uff00') + ((var19 & 16711935) * var13 >> 8 & 16711935) + var18, 255 - super.k.a);
                     --var15;
                  } while(var15 > 0);
               }
            }
         } else {
            var11 = var6 - var5;
            if (this.k.a == 0) {
               do {
                  var12 = (var10 & ~(var10 >> 31)) >> 8;
                  Client.a(var1, var9++, this.j[var12], 255 - super.k.a);
                  var10 += var8;
                  --var11;
               } while(var11 > 0);
            } else {
               var12 = this.k.a;
               var13 = 256 - this.k.a;

               do {
                  var14 = (var10 & ~(var10 >> 31)) >> 8;
                  var15 = this.j[var14];
                  var10 += var8;
                  var16 = ((var15 & '\uff00') * var13 >> 8 & '\uff00') + ((var15 & 16711935) * var13 >> 8 & 16711935);
                  var17 = var1[var9];
                  Client.a(var1, var9++, ((var17 & '\uff00') * var12 >> 8 & '\uff00') + ((var17 & 16711935) * var12 >> 8 & 16711935) + var16, 255 - super.k.a);
                  --var11;
               } while(var11 > 0);
            }
         }
      }

   }

   public void b(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10) {
      int var11 = (int)var4;
      int var12 = (int)var5;
      int var13 = (int)var6;
      int var14 = (int)var1;
      int var15 = (int)var2;
      int var16 = (int)var3;
      int var17 = 0;
      if (var14 != var15) {
         var17 = (var12 - var11 << 14) / (var15 - var14);
      }

      int var18 = 0;
      if (var15 != var16) {
         var18 = (var13 - var12 << 14) / (var16 - var15);
      }

      int var19 = 0;
      if (var14 != var16) {
         var19 = (var11 - var13 << 14) / (var14 - var16);
      }

      int[] var20 = this.k.l;
      int var21 = this.k.g;
      int var22;
      int var23;
      int var24;
      int var25;
      int var26;
      int var27;
      if (var14 <= var15 && var14 <= var16) {
         if (var14 < var21) {
            if (var15 > var21) {
               var15 = var21;
            }

            if (var16 > var21) {
               var16 = var21;
            }

            if (var15 < var16) {
               var23 = var22 = var11 << 14;
               if (var14 < 0) {
                  var23 -= var14 * var19;
                  var22 -= var14 * var17;
                  var14 = 0;
               }

               var24 = var12 << 14;
               if (var15 < 0) {
                  var24 -= var15 * var18;
                  var15 = 0;
               }

               if ((var14 == var15 || var19 >= var17) && (var14 != var15 || var19 <= var18)) {
                  var25 = var16 - var15;
                  var26 = var15 - var14;
                  var27 = var20[var14];

                  while(true) {
                     --var26;
                     if (var26 < 0) {
                        while(true) {
                           --var25;
                           if (var25 < 0) {
                              return;
                           }

                           this.a(aU.h, var27, var10, 0, var24 >> 14, var23 >> 14);
                           var23 += var19;
                           var24 += var18;
                           var27 += aU.f;
                        }
                     }

                     this.a(aU.h, var27, var10, 0, var22 >> 14, var23 >> 14);
                     var23 += var19;
                     var22 += var17;
                     var27 += aU.f;
                  }
               }

               var25 = var16 - var15;
               var26 = var15 - var14;
               var27 = var20[var14];

               while(true) {
                  --var26;
                  if (var26 < 0) {
                     while(true) {
                        --var25;
                        if (var25 < 0) {
                           return;
                        }

                        this.a(aU.h, var27, var10, 0, var23 >> 14, var24 >> 14);
                        var23 += var19;
                        var24 += var18;
                        var27 += aU.f;
                     }
                  }

                  this.a(aU.h, var27, var10, 0, var23 >> 14, var22 >> 14);
                  var23 += var19;
                  var22 += var17;
                  var27 += aU.f;
               }
            }

            var23 = var22 = var11 << 14;
            if (var14 < 0) {
               var23 -= var14 * var19;
               var22 -= var14 * var17;
               var14 = 0;
            }

            var24 = var13 << 14;
            if (var16 < 0) {
               var24 -= var16 * var18;
               var16 = 0;
            }

            if (var14 != var16 && var19 < var17 || var14 == var16 && var18 > var17) {
               var25 = var15 - var16;
               var26 = var16 - var14;
               var27 = var20[var14];

               while(true) {
                  --var26;
                  if (var26 < 0) {
                     while(true) {
                        --var25;
                        if (var25 < 0) {
                           return;
                        }

                        this.a(aU.h, var27, var10, 0, var24 >> 14, var22 >> 14);
                        var24 += var18;
                        var22 += var17;
                        var27 += aU.f;
                     }
                  }

                  this.a(aU.h, var27, var10, 0, var23 >> 14, var22 >> 14);
                  var23 += var19;
                  var22 += var17;
                  var27 += aU.f;
               }
            }

            var25 = var15 - var16;
            var26 = var16 - var14;
            var27 = var20[var14];

            while(true) {
               --var26;
               if (var26 < 0) {
                  while(true) {
                     --var25;
                     if (var25 < 0) {
                        return;
                     }

                     this.a(aU.h, var27, var10, 0, var22 >> 14, var24 >> 14);
                     var24 += var18;
                     var22 += var17;
                     var27 += aU.f;
                  }
               }

               this.a(aU.h, var27, var10, 0, var22 >> 14, var23 >> 14);
               var23 += var19;
               var22 += var17;
               var27 += aU.f;
            }
         }
      } else if (var15 <= var16) {
         if (var15 < var21) {
            if (var16 > var21) {
               var16 = var21;
            }

            if (var14 > var21) {
               var14 = var21;
            }

            if (var16 < var14) {
               var23 = var22 = var12 << 14;
               if (var15 < 0) {
                  var23 -= var15 * var17;
                  var22 -= var15 * var18;
                  var15 = 0;
               }

               var24 = var13 << 14;
               if (var16 < 0) {
                  var24 -= var16 * var19;
                  var16 = 0;
               }

               if ((var15 == var16 || var17 >= var18) && (var15 != var16 || var17 <= var19)) {
                  var25 = var14 - var16;
                  var26 = var16 - var15;
                  var27 = var20[var15];

                  while(true) {
                     --var26;
                     if (var26 < 0) {
                        while(true) {
                           --var25;
                           if (var25 < 0) {
                              return;
                           }

                           this.a(aU.h, var27, var10, 0, var24 >> 14, var23 >> 14);
                           var23 += var17;
                           var24 += var19;
                           var27 += aU.f;
                        }
                     }

                     this.a(aU.h, var27, var10, 0, var22 >> 14, var23 >> 14);
                     var23 += var17;
                     var22 += var18;
                     var27 += aU.f;
                  }
               }

               var25 = var14 - var16;
               var26 = var16 - var15;
               var27 = var20[var15];

               while(true) {
                  --var26;
                  if (var26 < 0) {
                     while(true) {
                        --var25;
                        if (var25 < 0) {
                           return;
                        }

                        this.a(aU.h, var27, var10, 0, var23 >> 14, var24 >> 14);
                        var23 += var17;
                        var24 += var19;
                        var27 += aU.f;
                     }
                  }

                  this.a(aU.h, var27, var10, 0, var23 >> 14, var22 >> 14);
                  var23 += var17;
                  var22 += var18;
                  var27 += aU.f;
               }
            }

            var23 = var22 = var12 << 14;
            if (var15 < 0) {
               var23 -= var15 * var17;
               var22 -= var15 * var18;
               var15 = 0;
            }

            var24 = var11 << 14;
            if (var14 < 0) {
               var24 -= var14 * var19;
               var14 = 0;
            }

            if (var17 < var18) {
               var25 = var16 - var14;
               var26 = var14 - var15;
               var27 = var20[var15];

               while(true) {
                  --var26;
                  if (var26 < 0) {
                     while(true) {
                        --var25;
                        if (var25 < 0) {
                           return;
                        }

                        this.a(aU.h, var27, var10, 0, var24 >> 14, var22 >> 14);
                        var24 += var19;
                        var22 += var18;
                        var27 += aU.f;
                     }
                  }

                  this.a(aU.h, var27, var10, 0, var23 >> 14, var22 >> 14);
                  var23 += var17;
                  var22 += var18;
                  var27 += aU.f;
               }
            }

            var25 = var16 - var14;
            var26 = var14 - var15;
            var27 = var20[var15];

            while(true) {
               --var26;
               if (var26 < 0) {
                  while(true) {
                     --var25;
                     if (var25 < 0) {
                        return;
                     }

                     this.a(aU.h, var27, var10, 0, var22 >> 14, var24 >> 14);
                     var24 += var19;
                     var22 += var18;
                     var27 += aU.f;
                  }
               }

               this.a(aU.h, var27, var10, 0, var22 >> 14, var23 >> 14);
               var23 += var17;
               var22 += var18;
               var27 += aU.f;
            }
         }
      } else if (var16 < var21) {
         if (var14 > var21) {
            var14 = var21;
         }

         if (var15 > var21) {
            var15 = var21;
         }

         if (var14 < var15) {
            var23 = var22 = var13 << 14;
            if (var16 < 0) {
               var23 -= var16 * var18;
               var22 -= var16 * var19;
               var16 = 0;
            }

            var24 = var11 << 14;
            if (var14 < 0) {
               var24 -= var14 * var17;
               var14 = 0;
            }

            if (var18 < var19) {
               var25 = var15 - var14;
               var26 = var14 - var16;
               var27 = var20[var16];

               while(true) {
                  --var26;
                  if (var26 < 0) {
                     while(true) {
                        --var25;
                        if (var25 < 0) {
                           return;
                        }

                        this.a(aU.h, var27, var10, 0, var23 >> 14, var24 >> 14);
                        var23 += var18;
                        var24 += var17;
                        var27 += aU.f;
                     }
                  }

                  this.a(aU.h, var27, var10, 0, var23 >> 14, var22 >> 14);
                  var23 += var18;
                  var22 += var19;
                  var27 += aU.f;
               }
            }

            var25 = var15 - var14;
            var26 = var14 - var16;
            var27 = var20[var16];

            while(true) {
               --var26;
               if (var26 < 0) {
                  while(true) {
                     --var25;
                     if (var25 < 0) {
                        return;
                     }

                     this.a(aU.h, var27, var10, 0, var24 >> 14, var23 >> 14);
                     var23 += var18;
                     var24 += var17;
                     var27 += aU.f;
                  }
               }

               this.a(aU.h, var27, var10, 0, var22 >> 14, var23 >> 14);
               var23 += var18;
               var22 += var19;
               var27 += aU.f;
            }
         }

         var23 = var22 = var13 << 14;
         if (var16 < 0) {
            var23 -= var16 * var18;
            var22 -= var16 * var19;
            var16 = 0;
         }

         var24 = var12 << 14;
         if (var15 < 0) {
            var24 -= var15 * var17;
            var15 = 0;
         }

         if (var18 < var19) {
            var25 = var14 - var15;
            var26 = var15 - var16;
            var27 = var20[var16];

            while(true) {
               --var26;
               if (var26 < 0) {
                  while(true) {
                     --var25;
                     if (var25 < 0) {
                        return;
                     }

                     this.a(aU.h, var27, var10, 0, var24 >> 14, var22 >> 14);
                     var24 += var17;
                     var22 += var19;
                     var27 += aU.f;
                  }
               }

               this.a(aU.h, var27, var10, 0, var23 >> 14, var22 >> 14);
               var23 += var18;
               var22 += var19;
               var27 += aU.f;
            }
         }

         var25 = var14 - var15;
         var26 = var15 - var16;
         var27 = var20[var16];

         while(true) {
            --var26;
            if (var26 < 0) {
               while(true) {
                  --var25;
                  if (var25 < 0) {
                     return;
                  }

                  this.a(aU.h, var27, var10, 0, var22 >> 14, var24 >> 14);
                  var24 += var17;
                  var22 += var19;
                  var27 += aU.f;
               }
            }

            this.a(aU.h, var27, var10, 0, var22 >> 14, var23 >> 14);
            var23 += var18;
            var22 += var19;
            var27 += aU.f;
         }
      }

   }

   public void a(int[] var1, int var2, int var3, int var4, int var5, int var6) {
      if (this.k.q) {
         if (var6 > this.k.f) {
            var6 = this.k.f;
         }

         if (var5 < 0) {
            var5 = 0;
         }
      }

      if (var5 < var6) {
         int var7 = var2 + var5;
         int var8 = var6 - var5 >> 2;
         int var9;
         if (this.k.a != 0) {
            if (this.k.a == 254) {
               while(true) {
                  --var8;
                  if (var8 < 0) {
                     var9 = var6 - var5 & 3;

                     while(true) {
                        --var9;
                        if (var9 < 0) {
                           return;
                        }

                        Client.a(var1, var7++, var1[var7], 255 - super.k.a);
                     }
                  }

                  Client.a(var1, var7++, var1[var7], 255 - super.k.a);
                  Client.a(var1, var7++, var1[var7], 255 - super.k.a);
                  Client.a(var1, var7++, var1[var7], 255 - super.k.a);
                  Client.a(var1, var7++, var1[var7], 255 - super.k.a);
               }
            } else {
               var9 = this.k.a;
               int var10 = 256 - this.k.a;
               int var11 = ((var3 & '\uff00') * var10 >> 8 & '\uff00') + ((var3 & 16711935) * var10 >> 8 & 16711935);

               while(true) {
                  --var8;
                  int var12;
                  int var13;
                  if (var8 < 0) {
                     var12 = var6 - var5 & 3;

                     while(true) {
                        --var12;
                        if (var12 < 0) {
                           return;
                        }

                        var13 = var1[var7];
                        Client.a(var1, var7++, ((var13 & '\uff00') * var9 >> 8 & '\uff00') + ((var13 & 16711935) * var9 >> 8 & 16711935) + var11, 255 - super.k.a);
                     }
                  }

                  var12 = var1[var7];
                  Client.a(var1, var7++, ((var12 & '\uff00') * var9 >> 8 & '\uff00') + ((var12 & 16711935) * var9 >> 8 & 16711935) + var11, 255 - super.k.a);
                  var13 = var1[var7];
                  Client.a(var1, var7++, ((var13 & '\uff00') * var9 >> 8 & '\uff00') + ((var13 & 16711935) * var9 >> 8 & 16711935) + var11, 255 - super.k.a);
                  int var14 = var1[var7];
                  Client.a(var1, var7++, ((var14 & '\uff00') * var9 >> 8 & '\uff00') + ((var14 & 16711935) * var9 >> 8 & 16711935) + var11, 255 - super.k.a);
                  int var15 = var1[var7];
                  Client.a(var1, var7++, ((var15 & '\uff00') * var9 >> 8 & '\uff00') + ((var15 & 16711935) * var9 >> 8 & 16711935) + var11, 255 - super.k.a);
               }
            }
         } else {
            while(true) {
               --var8;
               if (var8 < 0) {
                  var9 = var6 - var5 & 3;

                  while(true) {
                     --var9;
                     if (var9 < 0) {
                        return;
                     }

                     Client.a(var1, var7++, var3, 255 - super.k.a);
                  }
               }

               Client.a(var1, var7++, var3, 255 - super.k.a);
               Client.a(var1, var7++, var3, 255 - super.k.a);
               Client.a(var1, var7++, var3, 255 - super.k.a);
               Client.a(var1, var7++, var3, 255 - super.k.a);
            }
         }
      }
   }

   public void c(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21, int var22) {
      int[] var23 = this.k.c.d(var22);
      int var24;
      if (var23 == null) {
         var24 = this.k.c.b(var22);
         this.b(var1, var2, var3, var4, var5, var6, var7, var8, var9, a(var24, var10), a(var24, var11), a(var24, var12));
      } else {
         this.i = this.k.c.a(var22);
         var24 = (int)var4;
         int var25 = (int)var5;
         int var26 = (int)var6;
         int var27 = (int)var1;
         int var28 = (int)var2;
         int var29 = (int)var3;
         int var30 = var25 - var24;
         int var31 = var28 - var27;
         int var32 = var26 - var24;
         int var33 = var29 - var27;
         int var34 = var11 - var10;
         int var35 = var12 - var10;
         int var36 = 0;
         if (var27 != var28) {
            var36 = (var25 - var24 << 14) / (var28 - var27);
         }

         int var37 = 0;
         if (var28 != var29) {
            var37 = (var26 - var25 << 14) / (var29 - var28);
         }

         int var38 = 0;
         if (var27 != var29) {
            var38 = (var24 - var26 << 14) / (var27 - var29);
         }

         int var39 = var30 * var33 - var31 * var32;
         if (var39 != 0) {
            int var40 = (var33 * var34 - var31 * var35 << 9) / var39;
            int var41 = (var30 * var35 - var32 * var34 << 9) / var39;
            int var42 = this.k.b;
            int var43 = var13 - var14;
            int var44 = var16 - var17;
            int var45 = var19 - var20;
            int var46 = var15 - var13;
            int var47 = var18 - var16;
            int var48 = var21 - var19;
            int var49 = var16 * var46 - var13 * var47 << 14;
            int var50 = (int)(((long)(var19 * var47 - var16 * var48) << 3 << 14) / (long)var42);
            int var51 = (int)(((long)(var13 * var48 - var19 * var46) << 14) / (long)var42);
            int var52 = var16 * var43 - var13 * var44 << 14;
            int var53 = (int)(((long)(var19 * var44 - var16 * var45) << 3 << 14) / (long)var42);
            int var54 = (int)(((long)(var13 * var45 - var19 * var43) << 14) / (long)var42);
            int var55 = var44 * var46 - var43 * var47 << 14;
            int var56 = (int)(((long)(var45 * var47 - var44 * var48) << 3 << 14) / (long)var42);
            int var57 = (int)(((long)(var43 * var48 - var45 * var46) << 14) / (long)var42);
            int[] var58 = this.k.l;
            int var59 = this.k.g;
            int var60;
            int var61;
            int var62;
            int var63;
            int var64;
            int var65;
            int var66;
            int var67;
            int var68;
            int var69;
            int var70;
            if (var27 <= var28 && var27 <= var29) {
               if (var27 < var59) {
                  if (var28 > var59) {
                     var28 = var59;
                  }

                  if (var29 > var59) {
                     var29 = var59;
                  }

                  var60 = (var10 << 9) - var24 * var40 + var40;
                  if (var28 < var29) {
                     var62 = var61 = var24 << 14;
                     if (var27 < 0) {
                        var62 -= var27 * var38;
                        var61 -= var27 * var36;
                        var60 -= var27 * var41;
                        var27 = 0;
                     }

                     var63 = var25 << 14;
                     if (var28 < 0) {
                        var63 -= var28 * var37;
                        var28 = 0;
                     }

                     var64 = var27 - this.k.e;
                     var65 = var51 * var64 + var49;
                     var66 = var54 * var64 + var52;
                     var67 = var57 * var64 + var55;
                     if (var27 != var28 && var38 < var36 || var27 == var28 && var38 > var37) {
                        var68 = var29 - var28;
                        var69 = var28 - var27;
                        var70 = var58[var27];

                        while(true) {
                           --var69;
                           if (var69 < 0) {
                              while(true) {
                                 --var68;
                                 if (var68 < 0) {
                                    return;
                                 }

                                 this.a(aU.h, var23, 0, 0, var70, var62 >> 14, var63 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                                 var62 += var38;
                                 var63 += var37;
                                 var60 += var41;
                                 var70 += aU.f;
                                 var65 += var51;
                                 var66 += var54;
                                 var67 += var57;
                              }
                           }

                           this.a(aU.h, var23, 0, 0, var70, var62 >> 14, var61 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                           var62 += var38;
                           var61 += var36;
                           var60 += var41;
                           var70 += aU.f;
                           var65 += var51;
                           var66 += var54;
                           var67 += var57;
                        }
                     }

                     var68 = var29 - var28;
                     var69 = var28 - var27;
                     var70 = var58[var27];

                     while(true) {
                        --var69;
                        if (var69 < 0) {
                           while(true) {
                              --var68;
                              if (var68 < 0) {
                                 return;
                              }

                              this.a(aU.h, var23, 0, 0, var70, var63 >> 14, var62 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                              var62 += var38;
                              var63 += var37;
                              var60 += var41;
                              var70 += aU.f;
                              var65 += var51;
                              var66 += var54;
                              var67 += var57;
                           }
                        }

                        this.a(aU.h, var23, 0, 0, var70, var61 >> 14, var62 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                        var62 += var38;
                        var61 += var36;
                        var60 += var41;
                        var70 += aU.f;
                        var65 += var51;
                        var66 += var54;
                        var67 += var57;
                     }
                  }

                  var62 = var61 = var24 << 14;
                  if (var27 < 0) {
                     var62 -= var27 * var38;
                     var61 -= var27 * var36;
                     var60 -= var27 * var41;
                     var27 = 0;
                  }

                  var63 = var26 << 14;
                  if (var29 < 0) {
                     var63 -= var29 * var37;
                     var29 = 0;
                  }

                  var64 = var27 - this.k.e;
                  var65 = var51 * var64 + var49;
                  var66 = var54 * var64 + var52;
                  var67 = var57 * var64 + var55;
                  if (var27 != var29 && var38 < var36 || var27 == var29 && var37 > var36) {
                     var68 = var28 - var29;
                     var69 = var29 - var27;
                     var70 = var58[var27];

                     while(true) {
                        --var69;
                        if (var69 < 0) {
                           while(true) {
                              --var68;
                              if (var68 < 0) {
                                 return;
                              }

                              this.a(aU.h, var23, 0, 0, var70, var63 >> 14, var61 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                              var63 += var37;
                              var61 += var36;
                              var60 += var41;
                              var70 += aU.f;
                              var65 += var51;
                              var66 += var54;
                              var67 += var57;
                           }
                        }

                        this.a(aU.h, var23, 0, 0, var70, var62 >> 14, var61 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                        var62 += var38;
                        var61 += var36;
                        var60 += var41;
                        var70 += aU.f;
                        var65 += var51;
                        var66 += var54;
                        var67 += var57;
                     }
                  }

                  var68 = var28 - var29;
                  var69 = var29 - var27;
                  var70 = var58[var27];

                  while(true) {
                     --var69;
                     if (var69 < 0) {
                        while(true) {
                           --var68;
                           if (var68 < 0) {
                              return;
                           }

                           this.a(aU.h, var23, 0, 0, var70, var61 >> 14, var63 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                           var63 += var37;
                           var61 += var36;
                           var60 += var41;
                           var70 += aU.f;
                           var65 += var51;
                           var66 += var54;
                           var67 += var57;
                        }
                     }

                     this.a(aU.h, var23, 0, 0, var70, var61 >> 14, var62 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                     var62 += var38;
                     var61 += var36;
                     var60 += var41;
                     var70 += aU.f;
                     var65 += var51;
                     var66 += var54;
                     var67 += var57;
                  }
               }
            } else if (var28 <= var29) {
               if (var28 < var59) {
                  if (var29 > var59) {
                     var29 = var59;
                  }

                  if (var27 > var59) {
                     var27 = var59;
                  }

                  var60 = (var11 << 9) - var25 * var40 + var40;
                  if (var29 < var27) {
                     var62 = var61 = var25 << 14;
                     if (var28 < 0) {
                        var62 -= var28 * var36;
                        var61 -= var28 * var37;
                        var60 -= var28 * var41;
                        var28 = 0;
                     }

                     var63 = var26 << 14;
                     if (var29 < 0) {
                        var63 -= var29 * var38;
                        var29 = 0;
                     }

                     var64 = var28 - this.k.e;
                     var65 = var51 * var64 + var49;
                     var66 = var54 * var64 + var52;
                     var67 = var57 * var64 + var55;
                     if (var28 != var29 && var36 < var37 || var28 == var29 && var36 > var38) {
                        var68 = var27 - var29;
                        var69 = var29 - var28;
                        var70 = var58[var28];

                        while(true) {
                           --var69;
                           if (var69 < 0) {
                              while(true) {
                                 --var68;
                                 if (var68 < 0) {
                                    return;
                                 }

                                 this.a(aU.h, var23, 0, 0, var70, var62 >> 14, var63 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                                 var62 += var36;
                                 var63 += var38;
                                 var60 += var41;
                                 var70 += aU.f;
                                 var65 += var51;
                                 var66 += var54;
                                 var67 += var57;
                              }
                           }

                           this.a(aU.h, var23, 0, 0, var70, var62 >> 14, var61 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                           var62 += var36;
                           var61 += var37;
                           var60 += var41;
                           var70 += aU.f;
                           var65 += var51;
                           var66 += var54;
                           var67 += var57;
                        }
                     }

                     var68 = var27 - var29;
                     var69 = var29 - var28;
                     var70 = var58[var28];

                     while(true) {
                        --var69;
                        if (var69 < 0) {
                           while(true) {
                              --var68;
                              if (var68 < 0) {
                                 return;
                              }

                              this.a(aU.h, var23, 0, 0, var70, var63 >> 14, var62 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                              var62 += var36;
                              var63 += var38;
                              var60 += var41;
                              var70 += aU.f;
                              var65 += var51;
                              var66 += var54;
                              var67 += var57;
                           }
                        }

                        this.a(aU.h, var23, 0, 0, var70, var61 >> 14, var62 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                        var62 += var36;
                        var61 += var37;
                        var60 += var41;
                        var70 += aU.f;
                        var65 += var51;
                        var66 += var54;
                        var67 += var57;
                     }
                  }

                  var62 = var61 = var25 << 14;
                  if (var28 < 0) {
                     var62 -= var28 * var36;
                     var61 -= var28 * var37;
                     var60 -= var28 * var41;
                     var28 = 0;
                  }

                  var63 = var24 << 14;
                  if (var27 < 0) {
                     var63 -= var27 * var38;
                     var27 = 0;
                  }

                  var64 = var28 - this.k.e;
                  var65 = var51 * var64 + var49;
                  var66 = var54 * var64 + var52;
                  var67 = var57 * var64 + var55;
                  if (var36 < var37) {
                     var68 = var29 - var27;
                     var69 = var27 - var28;
                     var70 = var58[var28];

                     while(true) {
                        --var69;
                        if (var69 < 0) {
                           while(true) {
                              --var68;
                              if (var68 < 0) {
                                 return;
                              }

                              this.a(aU.h, var23, 0, 0, var70, var63 >> 14, var61 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                              var63 += var38;
                              var61 += var37;
                              var60 += var41;
                              var70 += aU.f;
                              var65 += var51;
                              var66 += var54;
                              var67 += var57;
                           }
                        }

                        this.a(aU.h, var23, 0, 0, var70, var62 >> 14, var61 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                        var62 += var36;
                        var61 += var37;
                        var60 += var41;
                        var70 += aU.f;
                        var65 += var51;
                        var66 += var54;
                        var67 += var57;
                     }
                  }

                  var68 = var29 - var27;
                  var69 = var27 - var28;
                  var70 = var58[var28];

                  while(true) {
                     --var69;
                     if (var69 < 0) {
                        while(true) {
                           --var68;
                           if (var68 < 0) {
                              return;
                           }

                           this.a(aU.h, var23, 0, 0, var70, var61 >> 14, var63 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                           var63 += var38;
                           var61 += var37;
                           var60 += var41;
                           var70 += aU.f;
                           var65 += var51;
                           var66 += var54;
                           var67 += var57;
                        }
                     }

                     this.a(aU.h, var23, 0, 0, var70, var61 >> 14, var62 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                     var62 += var36;
                     var61 += var37;
                     var60 += var41;
                     var70 += aU.f;
                     var65 += var51;
                     var66 += var54;
                     var67 += var57;
                  }
               }
            } else if (var29 < var59) {
               if (var27 > var59) {
                  var27 = var59;
               }

               if (var28 > var59) {
                  var28 = var59;
               }

               var60 = (var12 << 9) - var26 * var40 + var40;
               if (var27 < var28) {
                  var62 = var61 = var26 << 14;
                  if (var29 < 0) {
                     var62 -= var29 * var37;
                     var61 -= var29 * var38;
                     var60 -= var29 * var41;
                     var29 = 0;
                  }

                  var63 = var24 << 14;
                  if (var27 < 0) {
                     var63 -= var27 * var36;
                     var27 = 0;
                  }

                  var64 = var29 - this.k.e;
                  var65 = var51 * var64 + var49;
                  var66 = var54 * var64 + var52;
                  var67 = var57 * var64 + var55;
                  if (var37 < var38) {
                     var68 = var28 - var27;
                     var69 = var27 - var29;
                     var70 = var58[var29];

                     while(true) {
                        --var69;
                        if (var69 < 0) {
                           while(true) {
                              --var68;
                              if (var68 < 0) {
                                 return;
                              }

                              this.a(aU.h, var23, 0, 0, var70, var62 >> 14, var63 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                              var62 += var37;
                              var63 += var36;
                              var60 += var41;
                              var70 += aU.f;
                              var65 += var51;
                              var66 += var54;
                              var67 += var57;
                           }
                        }

                        this.a(aU.h, var23, 0, 0, var70, var62 >> 14, var61 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                        var62 += var37;
                        var61 += var38;
                        var60 += var41;
                        var70 += aU.f;
                        var65 += var51;
                        var66 += var54;
                        var67 += var57;
                     }
                  }

                  var68 = var28 - var27;
                  var69 = var27 - var29;
                  var70 = var58[var29];

                  while(true) {
                     --var69;
                     if (var69 < 0) {
                        while(true) {
                           --var68;
                           if (var68 < 0) {
                              return;
                           }

                           this.a(aU.h, var23, 0, 0, var70, var63 >> 14, var62 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                           var62 += var37;
                           var63 += var36;
                           var60 += var41;
                           var70 += aU.f;
                           var65 += var51;
                           var66 += var54;
                           var67 += var57;
                        }
                     }

                     this.a(aU.h, var23, 0, 0, var70, var61 >> 14, var62 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                     var62 += var37;
                     var61 += var38;
                     var60 += var41;
                     var70 += aU.f;
                     var65 += var51;
                     var66 += var54;
                     var67 += var57;
                  }
               }

               var62 = var61 = var26 << 14;
               if (var29 < 0) {
                  var62 -= var29 * var37;
                  var61 -= var29 * var38;
                  var60 -= var29 * var41;
                  var29 = 0;
               }

               var63 = var25 << 14;
               if (var28 < 0) {
                  var63 -= var28 * var36;
                  var28 = 0;
               }

               var64 = var29 - this.k.e;
               var65 = var51 * var64 + var49;
               var66 = var54 * var64 + var52;
               var67 = var57 * var64 + var55;
               if (var37 < var38) {
                  var68 = var27 - var28;
                  var69 = var28 - var29;
                  var70 = var58[var29];

                  while(true) {
                     --var69;
                     if (var69 < 0) {
                        while(true) {
                           --var68;
                           if (var68 < 0) {
                              return;
                           }

                           this.a(aU.h, var23, 0, 0, var70, var63 >> 14, var61 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                           var63 += var36;
                           var61 += var38;
                           var60 += var41;
                           var70 += aU.f;
                           var65 += var51;
                           var66 += var54;
                           var67 += var57;
                        }
                     }

                     this.a(aU.h, var23, 0, 0, var70, var62 >> 14, var61 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                     var62 += var37;
                     var61 += var38;
                     var60 += var41;
                     var70 += aU.f;
                     var65 += var51;
                     var66 += var54;
                     var67 += var57;
                  }
               }

               var68 = var27 - var28;
               var69 = var28 - var29;
               var70 = var58[var29];

               while(true) {
                  --var69;
                  if (var69 < 0) {
                     while(true) {
                        --var68;
                        if (var68 < 0) {
                           return;
                        }

                        this.a(aU.h, var23, 0, 0, var70, var61 >> 14, var63 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                        var63 += var36;
                        var61 += var38;
                        var60 += var41;
                        var70 += aU.f;
                        var65 += var51;
                        var66 += var54;
                        var67 += var57;
                     }
                  }

                  this.a(aU.h, var23, 0, 0, var70, var61 >> 14, var62 >> 14, var60, var40, var65, var66, var67, var50, var53, var56);
                  var62 += var37;
                  var61 += var38;
                  var60 += var41;
                  var70 += aU.f;
                  var65 += var51;
                  var66 += var54;
                  var67 += var57;
               }
            }
         }
      }

   }

   public void a(int[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15) {
      if (this.k.q) {
         if (var7 > this.k.f) {
            var7 = this.k.f;
         }

         if (var6 < 0) {
            var6 = 0;
         }
      }

      if (var6 < var7) {
         int var16 = var5 + var6;
         int var17 = var6 * var9 + var8;
         int var18 = var7 - var6;
         int var19 = var6 - this.k.d;
         int var20 = (var13 >> 3) * var19 + var10;
         int var21 = (var14 >> 3) * var19 + var11;
         int var22 = (var15 >> 3) * var19 + var12;
         int var23 = var22 >> 14;
         int var24;
         int var25;
         if (var23 != 0) {
            var24 = var20 / var23;
            var25 = var21 / var23;
            if (var24 < 0) {
               var24 = 0;
            } else if (var24 > 16256) {
               var24 = 16256;
            }
         } else {
            var24 = 0;
            var25 = 0;
         }

         int var26 = var13 + var20;
         int var27 = var14 + var21;
         int var28 = var15 + var22;
         int var29 = var28 >> 14;
         int var30;
         int var31;
         if (var29 != 0) {
            var30 = var26 / var29;
            var31 = var27 / var29;
            if (var30 < 0) {
               var30 = 0;
            } else if (var30 > 16256) {
               var30 = 16256;
            }
         } else {
            var30 = 0;
            var31 = 0;
         }

         int var32 = (var24 << 18) + var25;
         int var33 = (var30 - var24 >> 3 << 18) + (var31 - var25 >> 3);
         int var34 = var18 >> 3;
         int var35 = var9 << 3;
         int var36 = var17 >> 8;
         int var10000;
         int var38;
         int var39;
         int var40;
         int var41;
         int var42;
         int var43;
         int var44;
         int var45;
         int var46;
         int var47;
         int var48;
         int var49;
         int var50;
         int var51;
         int var52;
         int var53;
         int var54;
         int var55;
         int var56;
         int var57;
         int var58;
         int var59;
         if (this.i) {
            if (this.k.a == 0) {
               if (var34 > 0) {
                  do {
                     var38 = var2[(var32 >>> 25) + (var32 & 16256)];
                     var1[var16++] = ((var38 & '\uff00') * var36 & 16711680) + ((var38 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                     var39 = var32 + var33;
                     var40 = var2[(var39 >>> 25) + (var39 & 16256)];
                     var1[var16++] = ((var40 & '\uff00') * var36 & 16711680) + ((var40 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                     var41 = var33 + var39;
                     var42 = var2[(var41 >>> 25) + (var41 & 16256)];
                     var1[var16++] = ((var42 & '\uff00') * var36 & 16711680) + ((var42 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                     var43 = var33 + var41;
                     var44 = var2[(var43 >>> 25) + (var43 & 16256)];
                     var1[var16++] = ((var44 & '\uff00') * var36 & 16711680) + ((var44 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                     var45 = var33 + var43;
                     var46 = var2[(var45 >>> 25) + (var45 & 16256)];
                     var1[var16++] = ((var46 & '\uff00') * var36 & 16711680) + ((var46 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                     var47 = var33 + var45;
                     var48 = var2[(var47 >>> 25) + (var47 & 16256)];
                     var1[var16++] = ((var48 & '\uff00') * var36 & 16711680) + ((var48 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                     var49 = var33 + var47;
                     var50 = var2[(var49 >>> 25) + (var49 & 16256)];
                     var1[var16++] = ((var50 & '\uff00') * var36 & 16711680) + ((var50 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                     var51 = var33 + var49;
                     var52 = var2[(var51 >>> 25) + (var51 & 16256)];
                     var1[var16++] = ((var52 & '\uff00') * var36 & 16711680) + ((var52 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                     var10000 = var33 + var51;
                     var53 = var30;
                     var54 = var31;
                     var26 += var13;
                     var27 += var14;
                     var28 += var15;
                     var55 = var28 >> 14;
                     if (var55 != 0) {
                        var30 = var26 / var55;
                        var31 = var27 / var55;
                        if (var30 < 0) {
                           var30 = 0;
                        } else if (var30 > 16256) {
                           var30 = 16256;
                        }
                     } else {
                        var30 = 0;
                        var31 = 0;
                     }

                     var32 = (var53 << 18) + var54;
                     var33 = (var30 - var53 >> 3 << 18) + (var31 - var54 >> 3);
                     var17 += var35;
                     var36 = var17 >> 8;
                     --var34;
                  } while(var34 > 0);
               }

               var38 = var7 - var6 & 7;
               if (var38 > 0) {
                  do {
                     var39 = var2[(var32 >>> 25) + (var32 & 16256)];
                     var1[var16++] = ((var39 & '\uff00') * var36 & 16711680) + ((var39 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                     var32 += var33;
                     --var38;
                  } while(var38 > 0);
               }
            } else {
               var38 = this.k.a;
               var39 = 256 - this.k.a;
               var40 = var36 * var39 >> 8;
               if (var34 > 0) {
                  do {
                     var41 = var2[(var32 >>> 25) + (var32 & 16256)];
                     var42 = ((var41 & 16711935) * var40 & -16711936 | (var41 & '\uff00') * var40 & 16711680) >> 8;
                     var43 = var1[var16];
                     var1[var16++] = (((var43 & 16711935) * var38 & -16711936 | (var43 & '\uff00') * var38 & 16711680) >> 8) + var42 | -16777216;
                     var44 = var32 + var33;
                     var45 = var2[(var44 >>> 25) + (var44 & 16256)];
                     var46 = ((var45 & 16711935) * var40 & -16711936 | (var45 & '\uff00') * var40 & 16711680) >> 8;
                     var47 = var1[var16];
                     var1[var16++] = (((var47 & 16711935) * var38 & -16711936 | (var47 & '\uff00') * var38 & 16711680) >> 8) + var46 | -16777216;
                     var48 = var33 + var44;
                     var49 = var2[(var48 >>> 25) + (var48 & 16256)];
                     var50 = ((var49 & 16711935) * var40 & -16711936 | (var49 & '\uff00') * var40 & 16711680) >> 8;
                     var51 = var1[var16];
                     var1[var16++] = (((var51 & 16711935) * var38 & -16711936 | (var51 & '\uff00') * var38 & 16711680) >> 8) + var50 | -16777216;
                     var52 = var33 + var48;
                     var53 = var2[(var52 >>> 25) + (var52 & 16256)];
                     var54 = ((var53 & 16711935) * var40 & -16711936 | (var53 & '\uff00') * var40 & 16711680) >> 8;
                     var55 = var1[var16];
                     var1[var16++] = (((var55 & 16711935) * var38 & -16711936 | (var55 & '\uff00') * var38 & 16711680) >> 8) + var54 | -16777216;
                     var56 = var33 + var52;
                     var57 = var2[(var56 >>> 25) + (var56 & 16256)];
                     var58 = ((var57 & 16711935) * var40 & -16711936 | (var57 & '\uff00') * var40 & 16711680) >> 8;
                     var59 = var1[var16];
                     var1[var16++] = (((var59 & 16711935) * var38 & -16711936 | (var59 & '\uff00') * var38 & 16711680) >> 8) + var58 | -16777216;
                     int var60 = var33 + var56;
                     int var61 = var2[(var60 >>> 25) + (var60 & 16256)];
                     int var62 = ((var61 & 16711935) * var40 & -16711936 | (var61 & '\uff00') * var40 & 16711680) >> 8;
                     int var63 = var1[var16];
                     var1[var16++] = (((var63 & 16711935) * var38 & -16711936 | (var63 & '\uff00') * var38 & 16711680) >> 8) + var62 | -16777216;
                     int var64 = var33 + var60;
                     int var65 = var2[(var64 >>> 25) + (var64 & 16256)];
                     int var66 = ((var65 & 16711935) * var40 & -16711936 | (var65 & '\uff00') * var40 & 16711680) >> 8;
                     int var67 = var1[var16];
                     var1[var16++] = (((var67 & 16711935) * var38 & -16711936 | (var67 & '\uff00') * var38 & 16711680) >> 8) + var66 | -16777216;
                     int var68 = var33 + var64;
                     int var69 = var2[(var68 >>> 25) + (var68 & 16256)];
                     int var70 = ((var69 & 16711935) * var40 & -16711936 | (var69 & '\uff00') * var40 & 16711680) >> 8;
                     int var71 = var1[var16];
                     var1[var16++] = (((var71 & 16711935) * var38 & -16711936 | (var71 & '\uff00') * var38 & 16711680) >> 8) + var70 | -16777216;
                     var10000 = var33 + var68;
                     int var72 = var30;
                     int var73 = var31;
                     var26 += var13;
                     var27 += var14;
                     var28 += var15;
                     int var74 = var28 >> 14;
                     if (var74 != 0) {
                        var30 = var26 / var74;
                        var31 = var27 / var74;
                        if (var30 < 0) {
                           var30 = 0;
                        } else if (var30 > 16256) {
                           var30 = 16256;
                        }
                     } else {
                        var30 = 0;
                        var31 = 0;
                     }

                     var32 = (var72 << 18) + var73;
                     var33 = (var30 - var72 >> 3 << 18) + (var31 - var73 >> 3);
                     var17 += var35;
                     int var75 = var17 >> 8;
                     --var34;
                  } while(var34 > 0);
               }

               var41 = var7 - var6 & 7;
               if (var41 > 0) {
                  do {
                     var42 = var2[(var32 >>> 25) + (var32 & 16256)];
                     var43 = ((var42 & 16711935) * var40 & -16711936 | (var42 & '\uff00') * var40 & 16711680) >> 8;
                     var44 = var1[var16];
                     var1[var16++] = (((var44 & 16711935) * var38 & -16711936 | (var44 & '\uff00') * var38 & 16711680) >> 8) + var43 | -16777216;
                     var32 += var33;
                     --var41;
                  } while(var41 > 0);
               }
            }
         } else if (this.k.a == 0) {
            if (var34 > 0) {
               do {
                  if ((var38 = var2[(var32 >>> 25) + (var32 & 16256)]) != 0) {
                     var1[var16] = ((var38 & '\uff00') * var36 & 16711680) + ((var38 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var39 = var32 + var33;
                  if ((var40 = var2[(var39 >>> 25) + (var39 & 16256)]) != 0) {
                     var1[var16] = ((var40 & '\uff00') * var36 & 16711680) + ((var40 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var41 = var33 + var39;
                  if ((var42 = var2[(var41 >>> 25) + (var41 & 16256)]) != 0) {
                     var1[var16] = ((var42 & '\uff00') * var36 & 16711680) + ((var42 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var43 = var33 + var41;
                  if ((var44 = var2[(var43 >>> 25) + (var43 & 16256)]) != 0) {
                     var1[var16] = ((var44 & '\uff00') * var36 & 16711680) + ((var44 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var45 = var33 + var43;
                  if ((var46 = var2[(var45 >>> 25) + (var45 & 16256)]) != 0) {
                     var1[var16] = ((var46 & '\uff00') * var36 & 16711680) + ((var46 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var47 = var33 + var45;
                  if ((var48 = var2[(var47 >>> 25) + (var47 & 16256)]) != 0) {
                     var1[var16] = ((var48 & '\uff00') * var36 & 16711680) + ((var48 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var49 = var33 + var47;
                  if ((var50 = var2[(var49 >>> 25) + (var49 & 16256)]) != 0) {
                     var1[var16] = ((var50 & '\uff00') * var36 & 16711680) + ((var50 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var51 = var33 + var49;
                  if ((var52 = var2[(var51 >>> 25) + (var51 & 16256)]) != 0) {
                     var1[var16] = ((var52 & '\uff00') * var36 & 16711680) + ((var52 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var10000 = var33 + var51;
                  var53 = var30;
                  var54 = var31;
                  var26 += var13;
                  var27 += var14;
                  var28 += var15;
                  var55 = var28 >> 14;
                  if (var55 != 0) {
                     var30 = var26 / var55;
                     var31 = var27 / var55;
                     if (var30 < 0) {
                        var30 = 0;
                     } else if (var30 > 16256) {
                        var30 = 16256;
                     }
                  } else {
                     var30 = 0;
                     var31 = 0;
                  }

                  var32 = (var53 << 18) + var54;
                  var33 = (var30 - var53 >> 3 << 18) + (var31 - var54 >> 3);
                  var17 += var35;
                  var36 = var17 >> 8;
                  --var34;
               } while(var34 > 0);
            }

            var38 = var7 - var6 & 7;
            if (var38 > 0) {
               do {
                  if ((var39 = var2[(var32 >>> 25) + (var32 & 16256)]) != 0) {
                     var1[var16] = ((var39 & '\uff00') * var36 & 16711680) + ((var39 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var32 += var33;
                  --var38;
               } while(var38 > 0);
            }
         } else {
            var38 = this.k.a;
            var39 = 256 - this.k.a;
            var40 = var36 * var39 >> 8;
            if (var34 > 0) {
               do {
                  if ((var41 = var2[(var32 >>> 25) + (var32 & 16256)]) != 0) {
                     var42 = ((var41 & 16711935) * var40 & -16711936 | (var41 & '\uff00') * var40 & 16711680) >> 8;
                     var43 = var1[var16];
                     var1[var16] = (((var43 & 16711935) * var38 & -16711936 | (var43 & '\uff00') * var38 & 16711680) >> 8) + var42 | -16777216;
                  }

                  ++var16;
                  var42 = var32 + var33;
                  if ((var43 = var2[(var42 >>> 25) + (var42 & 16256)]) != 0) {
                     var44 = ((var43 & 16711935) * var40 & -16711936 | (var43 & '\uff00') * var40 & 16711680) >> 8;
                     var45 = var1[var16];
                     var1[var16] = (((var45 & 16711935) * var38 & -16711936 | (var45 & '\uff00') * var38 & 16711680) >> 8) + var44 | -16777216;
                  }

                  ++var16;
                  var44 = var33 + var42;
                  if ((var45 = var2[(var44 >>> 25) + (var44 & 16256)]) != 0) {
                     var46 = ((var45 & 16711935) * var40 & -16711936 | (var45 & '\uff00') * var40 & 16711680) >> 8;
                     var47 = var1[var16];
                     var1[var16] = (((var47 & 16711935) * var38 & -16711936 | (var47 & '\uff00') * var38 & 16711680) >> 8) + var46 | -16777216;
                  }

                  ++var16;
                  var46 = var33 + var44;
                  if ((var47 = var2[(var46 >>> 25) + (var46 & 16256)]) != 0) {
                     var48 = ((var47 & 16711935) * var40 & -16711936 | (var47 & '\uff00') * var40 & 16711680) >> 8;
                     var49 = var1[var16];
                     var1[var16] = (((var49 & 16711935) * var38 & -16711936 | (var49 & '\uff00') * var38 & 16711680) >> 8) + var48 | -16777216;
                  }

                  ++var16;
                  var48 = var33 + var46;
                  if ((var49 = var2[(var48 >>> 25) + (var48 & 16256)]) != 0) {
                     var50 = ((var49 & 16711935) * var40 & -16711936 | (var49 & '\uff00') * var40 & 16711680) >> 8;
                     var51 = var1[var16];
                     var1[var16] = (((var51 & 16711935) * var38 & -16711936 | (var51 & '\uff00') * var38 & 16711680) >> 8) + var50 | -16777216;
                  }

                  ++var16;
                  var50 = var33 + var48;
                  if ((var51 = var2[(var50 >>> 25) + (var50 & 16256)]) != 0) {
                     var52 = ((var51 & 16711935) * var40 & -16711936 | (var51 & '\uff00') * var40 & 16711680) >> 8;
                     var53 = var1[var16];
                     var1[var16] = (((var53 & 16711935) * var38 & -16711936 | (var53 & '\uff00') * var38 & 16711680) >> 8) + var52 | -16777216;
                  }

                  ++var16;
                  var52 = var33 + var50;
                  if ((var53 = var2[(var52 >>> 25) + (var52 & 16256)]) != 0) {
                     var54 = ((var53 & 16711935) * var40 & -16711936 | (var53 & '\uff00') * var40 & 16711680) >> 8;
                     var55 = var1[var16];
                     var1[var16] = (((var55 & 16711935) * var38 & -16711936 | (var55 & '\uff00') * var38 & 16711680) >> 8) + var54 | -16777216;
                  }

                  ++var16;
                  var54 = var33 + var52;
                  if ((var55 = var2[(var54 >>> 25) + (var54 & 16256)]) != 0) {
                     var56 = ((var55 & 16711935) * var40 & -16711936 | (var55 & '\uff00') * var40 & 16711680) >> 8;
                     var57 = var1[var16];
                     var1[var16] = (((var57 & 16711935) * var38 & -16711936 | (var57 & '\uff00') * var38 & 16711680) >> 8) + var56 | -16777216;
                  }

                  ++var16;
                  var10000 = var33 + var54;
                  var56 = var30;
                  var57 = var31;
                  var26 += var13;
                  var27 += var14;
                  var28 += var15;
                  var58 = var28 >> 14;
                  if (var58 != 0) {
                     var30 = var26 / var58;
                     var31 = var27 / var58;
                     if (var30 < 0) {
                        var30 = 0;
                     } else if (var30 > 16256) {
                        var30 = 16256;
                     }
                  } else {
                     var30 = 0;
                     var31 = 0;
                  }

                  var32 = (var56 << 18) + var57;
                  var33 = (var30 - var56 >> 3 << 18) + (var31 - var57 >> 3);
                  var17 += var35;
                  var59 = var17 >> 8;
                  --var34;
               } while(var34 > 0);
            }

            var41 = var7 - var6 & 7;
            if (var41 > 0) {
               do {
                  if ((var42 = var2[(var32 >>> 25) + (var32 & 16256)]) != 0) {
                     var43 = ((var42 & 16711935) * var40 & -16711936 | (var42 & '\uff00') * var40 & 16711680) >> 8;
                     var44 = var1[var16];
                     var1[var16] = (((var44 & 16711935) * var38 & -16711936 | (var44 & '\uff00') * var38 & 16711680) >> 8) + var43 | -16777216;
                  }

                  ++var16;
                  var32 += var33;
                  --var41;
               } while(var41 > 0);
            }
         }
      }

   }

   public void b(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21, int var22) {
      int[] var23 = this.k.c.d(var22);
      int var24;
      if (var23 == null) {
         var24 = this.k.c.b(var22);
         this.b(var1, var2, var3, var4, var5, var6, var7, var8, var9, a(var24, var10), a(var24, var11), a(var24, var12));
      } else {
         this.i = this.k.c.a(var22);
         var24 = (int)var4;
         int var25 = (int)var5;
         int var26 = (int)var6;
         int var27 = (int)var1;
         int var28 = (int)var2;
         int var29 = (int)var3;
         int var30 = var25 - var24;
         int var31 = var28 - var27;
         int var32 = var26 - var24;
         int var33 = var29 - var27;
         int var34 = var11 - var10;
         int var35 = var12 - var10;
         int var36 = 0;
         if (var27 != var28) {
            var36 = (var25 - var24 << 14) / (var28 - var27);
         }

         int var37 = 0;
         if (var28 != var29) {
            var37 = (var26 - var25 << 14) / (var29 - var28);
         }

         int var38 = 0;
         if (var27 != var29) {
            var38 = (var24 - var26 << 14) / (var27 - var29);
         }

         int var39 = var30 * var33 - var31 * var32;
         if (var39 != 0) {
            int var40 = (var33 * var34 - var31 * var35 << 9) / var39;
            int var41 = (var30 * var35 - var32 * var34 << 9) / var39;
            int var42 = this.k.b;
            int var43 = var13 - var14;
            int var44 = var16 - var17;
            int var45 = var19 - var20;
            int var46 = var15 - var13;
            int var47 = var18 - var16;
            int var48 = var21 - var19;
            int var49 = var16 * var46 - var13 * var47 << 14;
            int var50 = (int)(((long)(var19 * var47 - var16 * var48) << 14) / (long)var42);
            int var51 = (int)(((long)(var13 * var48 - var19 * var46) << 14) / (long)var42);
            int var52 = var16 * var43 - var13 * var44 << 14;
            int var53 = (int)(((long)(var19 * var44 - var16 * var45) << 14) / (long)var42);
            int var54 = (int)(((long)(var13 * var45 - var19 * var43) << 14) / (long)var42);
            int var55 = var44 * var46 - var43 * var47 << 14;
            int var56 = (int)(((long)(var45 * var47 - var44 * var48) << 14) / (long)var42);
            int var57 = (int)(((long)(var43 * var48 - var45 * var46) << 14) / (long)var42);
            int[] var58 = this.k.l;
            int var59 = this.k.g;
            int var60 = this.k.e;
            int var61;
            int var62;
            int var63;
            int var64;
            int var65;
            int var66;
            int var67;
            int var68;
            int var69;
            int var70;
            int var71;
            if (var27 <= var28 && var27 <= var29) {
               if (var27 < var59) {
                  if (var28 > var59) {
                     var28 = var59;
                  }

                  if (var29 > var59) {
                     var29 = var59;
                  }

                  var61 = (var10 << 9) - var24 * var40 + var40;
                  if (var28 < var29) {
                     var63 = var62 = var24 << 14;
                     if (var27 < 0) {
                        var63 -= var27 * var38;
                        var62 -= var27 * var36;
                        var61 -= var27 * var41;
                        var27 = 0;
                     }

                     var64 = var25 << 14;
                     if (var28 < 0) {
                        var64 -= var28 * var37;
                        var28 = 0;
                     }

                     var65 = var27 - var60;
                     var66 = var51 * var65 + var49;
                     var67 = var54 * var65 + var52;
                     var68 = var57 * var65 + var55;
                     if (var27 != var28 && var38 < var36 || var27 == var28 && var38 > var37) {
                        var69 = var29 - var28;
                        var70 = var28 - var27;
                        var71 = var58[var27];

                        while(true) {
                           --var70;
                           if (var70 < 0) {
                              while(true) {
                                 --var69;
                                 if (var69 < 0) {
                                    return;
                                 }

                                 this.b(aU.h, var23, 0, 0, var71, var63 >> 14, var64 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                                 var63 += var38;
                                 var64 += var37;
                                 var61 += var41;
                                 var71 += aU.f;
                                 var66 += var51;
                                 var67 += var54;
                                 var68 += var57;
                              }
                           }

                           this.b(aU.h, var23, 0, 0, var71, var63 >> 14, var62 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                           var63 += var38;
                           var62 += var36;
                           var61 += var41;
                           var71 += aU.f;
                           var66 += var51;
                           var67 += var54;
                           var68 += var57;
                        }
                     }

                     var69 = var29 - var28;
                     var70 = var28 - var27;
                     var71 = var58[var27];

                     while(true) {
                        --var70;
                        if (var70 < 0) {
                           while(true) {
                              --var69;
                              if (var69 < 0) {
                                 return;
                              }

                              this.b(aU.h, var23, 0, 0, var71, var64 >> 14, var63 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                              var63 += var38;
                              var64 += var37;
                              var61 += var41;
                              var71 += aU.f;
                              var66 += var51;
                              var67 += var54;
                              var68 += var57;
                           }
                        }

                        this.b(aU.h, var23, 0, 0, var71, var62 >> 14, var63 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                        var63 += var38;
                        var62 += var36;
                        var61 += var41;
                        var71 += aU.f;
                        var66 += var51;
                        var67 += var54;
                        var68 += var57;
                     }
                  }

                  var63 = var62 = var24 << 14;
                  if (var27 < 0) {
                     var63 -= var27 * var38;
                     var62 -= var27 * var36;
                     var61 -= var27 * var41;
                     var27 = 0;
                  }

                  var64 = var26 << 14;
                  if (var29 < 0) {
                     var64 -= var29 * var37;
                     var29 = 0;
                  }

                  var65 = var27 - var60;
                  var66 = var51 * var65 + var49;
                  var67 = var54 * var65 + var52;
                  var68 = var57 * var65 + var55;
                  if (var27 != var29 && var38 < var36 || var27 == var29 && var37 > var36) {
                     var69 = var28 - var29;
                     var70 = var29 - var27;
                     var71 = var58[var27];

                     while(true) {
                        --var70;
                        if (var70 < 0) {
                           while(true) {
                              --var69;
                              if (var69 < 0) {
                                 return;
                              }

                              this.b(aU.h, var23, 0, 0, var71, var64 >> 14, var62 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                              var64 += var37;
                              var62 += var36;
                              var61 += var41;
                              var71 += aU.f;
                              var66 += var51;
                              var67 += var54;
                              var68 += var57;
                           }
                        }

                        this.b(aU.h, var23, 0, 0, var71, var63 >> 14, var62 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                        var63 += var38;
                        var62 += var36;
                        var61 += var41;
                        var71 += aU.f;
                        var66 += var51;
                        var67 += var54;
                        var68 += var57;
                     }
                  }

                  var69 = var28 - var29;
                  var70 = var29 - var27;
                  var71 = var58[var27];

                  while(true) {
                     --var70;
                     if (var70 < 0) {
                        while(true) {
                           --var69;
                           if (var69 < 0) {
                              return;
                           }

                           this.b(aU.h, var23, 0, 0, var71, var62 >> 14, var64 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                           var64 += var37;
                           var62 += var36;
                           var61 += var41;
                           var71 += aU.f;
                           var66 += var51;
                           var67 += var54;
                           var68 += var57;
                        }
                     }

                     this.b(aU.h, var23, 0, 0, var71, var62 >> 14, var63 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                     var63 += var38;
                     var62 += var36;
                     var61 += var41;
                     var71 += aU.f;
                     var66 += var51;
                     var67 += var54;
                     var68 += var57;
                  }
               }
            } else if (var28 <= var29) {
               if (var28 < var59) {
                  if (var29 > var59) {
                     var29 = var59;
                  }

                  if (var27 > var59) {
                     var27 = var59;
                  }

                  var61 = (var11 << 9) - var25 * var40 + var40;
                  if (var29 < var27) {
                     var63 = var62 = var25 << 14;
                     if (var28 < 0) {
                        var63 -= var28 * var36;
                        var62 -= var28 * var37;
                        var61 -= var28 * var41;
                        var28 = 0;
                     }

                     var64 = var26 << 14;
                     if (var29 < 0) {
                        var64 -= var29 * var38;
                        var29 = 0;
                     }

                     var65 = var28 - var60;
                     var66 = var51 * var65 + var49;
                     var67 = var54 * var65 + var52;
                     var68 = var57 * var65 + var55;
                     if (var28 != var29 && var36 < var37 || var28 == var29 && var36 > var38) {
                        var69 = var27 - var29;
                        var70 = var29 - var28;
                        var71 = var58[var28];

                        while(true) {
                           --var70;
                           if (var70 < 0) {
                              while(true) {
                                 --var69;
                                 if (var69 < 0) {
                                    return;
                                 }

                                 this.b(aU.h, var23, 0, 0, var71, var63 >> 14, var64 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                                 var63 += var36;
                                 var64 += var38;
                                 var61 += var41;
                                 var71 += aU.f;
                                 var66 += var51;
                                 var67 += var54;
                                 var68 += var57;
                              }
                           }

                           this.b(aU.h, var23, 0, 0, var71, var63 >> 14, var62 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                           var63 += var36;
                           var62 += var37;
                           var61 += var41;
                           var71 += aU.f;
                           var66 += var51;
                           var67 += var54;
                           var68 += var57;
                        }
                     }

                     var69 = var27 - var29;
                     var70 = var29 - var28;
                     var71 = var58[var28];

                     while(true) {
                        --var70;
                        if (var70 < 0) {
                           while(true) {
                              --var69;
                              if (var69 < 0) {
                                 return;
                              }

                              this.b(aU.h, var23, 0, 0, var71, var64 >> 14, var63 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                              var63 += var36;
                              var64 += var38;
                              var61 += var41;
                              var71 += aU.f;
                              var66 += var51;
                              var67 += var54;
                              var68 += var57;
                           }
                        }

                        this.b(aU.h, var23, 0, 0, var71, var62 >> 14, var63 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                        var63 += var36;
                        var62 += var37;
                        var61 += var41;
                        var71 += aU.f;
                        var66 += var51;
                        var67 += var54;
                        var68 += var57;
                     }
                  }

                  var63 = var62 = var25 << 14;
                  if (var28 < 0) {
                     var63 -= var28 * var36;
                     var62 -= var28 * var37;
                     var61 -= var28 * var41;
                     var28 = 0;
                  }

                  var64 = var24 << 14;
                  if (var27 < 0) {
                     var64 -= var27 * var38;
                     var27 = 0;
                  }

                  var65 = var28 - var60;
                  var66 = var51 * var65 + var49;
                  var67 = var54 * var65 + var52;
                  var68 = var57 * var65 + var55;
                  if (var36 < var37) {
                     var69 = var29 - var27;
                     var70 = var27 - var28;
                     var71 = var58[var28];

                     while(true) {
                        --var70;
                        if (var70 < 0) {
                           while(true) {
                              --var69;
                              if (var69 < 0) {
                                 return;
                              }

                              this.b(aU.h, var23, 0, 0, var71, var64 >> 14, var62 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                              var64 += var38;
                              var62 += var37;
                              var61 += var41;
                              var71 += aU.f;
                              var66 += var51;
                              var67 += var54;
                              var68 += var57;
                           }
                        }

                        this.b(aU.h, var23, 0, 0, var71, var63 >> 14, var62 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                        var63 += var36;
                        var62 += var37;
                        var61 += var41;
                        var71 += aU.f;
                        var66 += var51;
                        var67 += var54;
                        var68 += var57;
                     }
                  }

                  var69 = var29 - var27;
                  var70 = var27 - var28;
                  var71 = var58[var28];

                  while(true) {
                     --var70;
                     if (var70 < 0) {
                        while(true) {
                           --var69;
                           if (var69 < 0) {
                              return;
                           }

                           this.b(aU.h, var23, 0, 0, var71, var62 >> 14, var64 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                           var64 += var38;
                           var62 += var37;
                           var61 += var41;
                           var71 += aU.f;
                           var66 += var51;
                           var67 += var54;
                           var68 += var57;
                        }
                     }

                     this.b(aU.h, var23, 0, 0, var71, var62 >> 14, var63 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                     var63 += var36;
                     var62 += var37;
                     var61 += var41;
                     var71 += aU.f;
                     var66 += var51;
                     var67 += var54;
                     var68 += var57;
                  }
               }
            } else if (var29 < var59) {
               if (var27 > var59) {
                  var27 = var59;
               }

               if (var28 > var59) {
                  var28 = var59;
               }

               var61 = (var12 << 9) - var26 * var40 + var40;
               if (var27 < var28) {
                  var63 = var62 = var26 << 14;
                  if (var29 < 0) {
                     var63 -= var29 * var37;
                     var62 -= var29 * var38;
                     var61 -= var29 * var41;
                     var29 = 0;
                  }

                  var64 = var24 << 14;
                  if (var27 < 0) {
                     var64 -= var27 * var36;
                     var27 = 0;
                  }

                  var65 = var29 - var60;
                  var66 = var51 * var65 + var49;
                  var67 = var54 * var65 + var52;
                  var68 = var57 * var65 + var55;
                  if (var37 < var38) {
                     var69 = var28 - var27;
                     var70 = var27 - var29;
                     var71 = var58[var29];

                     while(true) {
                        --var70;
                        if (var70 < 0) {
                           while(true) {
                              --var69;
                              if (var69 < 0) {
                                 return;
                              }

                              this.b(aU.h, var23, 0, 0, var71, var63 >> 14, var64 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                              var63 += var37;
                              var64 += var36;
                              var61 += var41;
                              var71 += aU.f;
                              var66 += var51;
                              var67 += var54;
                              var68 += var57;
                           }
                        }

                        this.b(aU.h, var23, 0, 0, var71, var63 >> 14, var62 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                        var63 += var37;
                        var62 += var38;
                        var61 += var41;
                        var71 += aU.f;
                        var66 += var51;
                        var67 += var54;
                        var68 += var57;
                     }
                  }

                  var69 = var28 - var27;
                  var70 = var27 - var29;
                  var71 = var58[var29];

                  while(true) {
                     --var70;
                     if (var70 < 0) {
                        while(true) {
                           --var69;
                           if (var69 < 0) {
                              return;
                           }

                           this.b(aU.h, var23, 0, 0, var71, var64 >> 14, var63 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                           var63 += var37;
                           var64 += var36;
                           var61 += var41;
                           var71 += aU.f;
                           var66 += var51;
                           var67 += var54;
                           var68 += var57;
                        }
                     }

                     this.b(aU.h, var23, 0, 0, var71, var62 >> 14, var63 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                     var63 += var37;
                     var62 += var38;
                     var61 += var41;
                     var71 += aU.f;
                     var66 += var51;
                     var67 += var54;
                     var68 += var57;
                  }
               }

               var63 = var62 = var26 << 14;
               if (var29 < 0) {
                  var63 -= var29 * var37;
                  var62 -= var29 * var38;
                  var61 -= var29 * var41;
                  var29 = 0;
               }

               var64 = var25 << 14;
               if (var28 < 0) {
                  var64 -= var28 * var36;
                  var28 = 0;
               }

               var65 = var29 - var60;
               var66 = var51 * var65 + var49;
               var67 = var54 * var65 + var52;
               var68 = var57 * var65 + var55;
               if (var37 < var38) {
                  var69 = var27 - var28;
                  var70 = var28 - var29;
                  var71 = var58[var29];

                  while(true) {
                     --var70;
                     if (var70 < 0) {
                        while(true) {
                           --var69;
                           if (var69 < 0) {
                              return;
                           }

                           this.b(aU.h, var23, 0, 0, var71, var64 >> 14, var62 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                           var64 += var36;
                           var62 += var38;
                           var61 += var41;
                           var71 += aU.f;
                           var66 += var51;
                           var67 += var54;
                           var68 += var57;
                        }
                     }

                     this.b(aU.h, var23, 0, 0, var71, var63 >> 14, var62 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                     var63 += var37;
                     var62 += var38;
                     var61 += var41;
                     var71 += aU.f;
                     var66 += var51;
                     var67 += var54;
                     var68 += var57;
                  }
               }

               var69 = var27 - var28;
               var70 = var28 - var29;
               var71 = var58[var29];

               while(true) {
                  --var70;
                  if (var70 < 0) {
                     while(true) {
                        --var69;
                        if (var69 < 0) {
                           return;
                        }

                        this.b(aU.h, var23, 0, 0, var71, var62 >> 14, var64 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                        var64 += var36;
                        var62 += var38;
                        var61 += var41;
                        var71 += aU.f;
                        var66 += var51;
                        var67 += var54;
                        var68 += var57;
                     }
                  }

                  this.b(aU.h, var23, 0, 0, var71, var62 >> 14, var63 >> 14, var61, var40, var66, var67, var68, var50, var53, var56);
                  var63 += var37;
                  var62 += var38;
                  var61 += var41;
                  var71 += aU.f;
                  var66 += var51;
                  var67 += var54;
                  var68 += var57;
               }
            }
         }
      }

   }

   public void b(int[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15) {
      if (this.k.q) {
         if (var7 > this.k.f) {
            var7 = this.k.f;
         }

         if (var6 < 0) {
            var6 = 0;
         }
      }

      if (var6 < var7) {
         int var16 = var5 + var6;
         int var17 = var6 * var9 + var8;
         int var18 = var7 - var6;
         int var19 = var6 - this.k.d;
         int var20 = var13 * var19 + var10;
         int var21 = var14 * var19 + var11;
         int var22 = var15 * var19 + var12;
         int var23 = var22 >> 14;
         int var24;
         int var25;
         if (var23 != 0) {
            var24 = var20 / var23;
            var25 = var21 / var23;
         } else {
            var24 = 0;
            var25 = 0;
         }

         int var26 = var13 * var18 + var20;
         int var27 = var14 * var18 + var21;
         int var28 = var15 * var18 + var22;
         int var29 = var28 >> 14;
         int var30;
         int var31;
         if (var29 != 0) {
            var30 = var26 / var29;
            var31 = var27 / var29;
         } else {
            var30 = 0;
            var31 = 0;
         }

         int var32 = (var24 << 18) + var25;
         int var33 = ((var30 - var24) / var18 << 18) + (var31 - var25) / var18;
         int var34 = var18 >> 3;
         int var35 = var9 << 3;
         int var36 = var17 >> 8;
         int var37;
         int var38;
         int var39;
         int var40;
         int var41;
         int var42;
         int var43;
         int var44;
         int var45;
         int var46;
         int var47;
         int var48;
         int var49;
         int var50;
         int var51;
         if (this.i) {
            if (var34 > 0) {
               do {
                  var37 = var2[(var32 >>> 25) + (var32 & 16256)];
                  var1[var16++] = ((var37 & '\uff00') * var36 & 16711680) + ((var37 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  var38 = var32 + var33;
                  var39 = var2[(var38 >>> 25) + (var38 & 16256)];
                  var1[var16++] = ((var39 & '\uff00') * var36 & 16711680) + ((var39 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  var40 = var33 + var38;
                  var41 = var2[(var40 >>> 25) + (var40 & 16256)];
                  var1[var16++] = ((var41 & '\uff00') * var36 & 16711680) + ((var41 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  var42 = var33 + var40;
                  var43 = var2[(var42 >>> 25) + (var42 & 16256)];
                  var1[var16++] = ((var43 & '\uff00') * var36 & 16711680) + ((var43 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  var44 = var33 + var42;
                  var45 = var2[(var44 >>> 25) + (var44 & 16256)];
                  var1[var16++] = ((var45 & '\uff00') * var36 & 16711680) + ((var45 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  var46 = var33 + var44;
                  var47 = var2[(var46 >>> 25) + (var46 & 16256)];
                  var1[var16++] = ((var47 & '\uff00') * var36 & 16711680) + ((var47 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  var48 = var33 + var46;
                  var49 = var2[(var48 >>> 25) + (var48 & 16256)];
                  var1[var16++] = ((var49 & '\uff00') * var36 & 16711680) + ((var49 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  var50 = var33 + var48;
                  var51 = var2[(var50 >>> 25) + (var50 & 16256)];
                  var1[var16++] = ((var51 & '\uff00') * var36 & 16711680) + ((var51 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  var32 = var33 + var50;
                  var17 += var35;
                  var36 = var17 >> 8;
                  --var34;
               } while(var34 > 0);
            }

            var37 = var7 - var6 & 7;
            if (var37 > 0) {
               do {
                  var38 = var2[(var32 >>> 25) + (var32 & 16256)];
                  var1[var16++] = ((var38 & '\uff00') * var36 & 16711680) + ((var38 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  var32 += var33;
                  --var37;
               } while(var37 > 0);
            }
         } else {
            if (var34 > 0) {
               do {
                  if ((var37 = var2[(var32 >>> 25) + (var32 & 16256)]) != 0) {
                     var1[var16] = ((var37 & '\uff00') * var36 & 16711680) + ((var37 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var38 = var32 + var33;
                  if ((var39 = var2[(var38 >>> 25) + (var38 & 16256)]) != 0) {
                     var1[var16] = ((var39 & '\uff00') * var36 & 16711680) + ((var39 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var40 = var33 + var38;
                  if ((var41 = var2[(var40 >>> 25) + (var40 & 16256)]) != 0) {
                     var1[var16] = ((var41 & '\uff00') * var36 & 16711680) + ((var41 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var42 = var33 + var40;
                  if ((var43 = var2[(var42 >>> 25) + (var42 & 16256)]) != 0) {
                     var1[var16] = ((var43 & '\uff00') * var36 & 16711680) + ((var43 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var44 = var33 + var42;
                  if ((var45 = var2[(var44 >>> 25) + (var44 & 16256)]) != 0) {
                     var1[var16] = ((var45 & '\uff00') * var36 & 16711680) + ((var45 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var46 = var33 + var44;
                  if ((var47 = var2[(var46 >>> 25) + (var46 & 16256)]) != 0) {
                     var1[var16] = ((var47 & '\uff00') * var36 & 16711680) + ((var47 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var48 = var33 + var46;
                  if ((var49 = var2[(var48 >>> 25) + (var48 & 16256)]) != 0) {
                     var1[var16] = ((var49 & '\uff00') * var36 & 16711680) + ((var49 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var50 = var33 + var48;
                  if ((var51 = var2[(var50 >>> 25) + (var50 & 16256)]) != 0) {
                     var1[var16] = ((var51 & '\uff00') * var36 & 16711680) + ((var51 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var32 = var33 + var50;
                  var17 += var35;
                  var36 = var17 >> 8;
                  --var34;
               } while(var34 > 0);
            }

            var37 = var7 - var6 & 7;
            if (var37 > 0) {
               do {
                  if ((var38 = var2[(var32 >>> 25) + (var32 & 16256)]) != 0) {
                     var1[var16] = ((var38 & '\uff00') * var36 & 16711680) + ((var38 & 16711935) * var36 & -16711936) >> 8 | -16777216;
                  }

                  ++var16;
                  var32 += var33;
                  --var37;
               } while(var37 > 0);
            }
         }
      }

   }
}
