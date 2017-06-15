
    #include <AR/gsub_es.h>
    #include <Eden/glm.h>
    #include <jni.h>
    #include <ARWrapper/ARToolKitWrapperExportedAPI.h>
    #include <unistd.h> // chdir()
    #include <android/log.h>

    // Utility preprocessor directive so only one change needed if Java class name changes
    #define JNIFUNCTION_DEMO(sig) Java_com_lucidleanlabs_dev_lcatalog_ar_ARNativeRenderer_##sig

        extern "C" {
            JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoInitialise(JNIEnv * env, jobject object)) ;
            JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoShutdown(JNIEnv * env, jobject object)) ;
            JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceCreated(JNIEnv * env, jobject object)) ;
            JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceChanged(JNIEnv * env, jobject object, jint w, jint h)) ;
            JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoDrawFrame(JNIEnv * env, jobject obj)) ;
        };

        typedef struct ARModel {
            int patternID;
            ARdouble transformationMatrix[16];
            bool visible;
            GLMmodel *obj;
        } ARModel;

        #define NUM_MODELS 10
        static ARModel models[NUM_MODELS] = {0};

        static float lightAmbient[4] = {0.1f, 0.1f, 0.1f, 1.0f};
        static float lightDiffuse[4] = {1.0f, 1.0f, 1.0f, 1.0f};
        static float lightPosition[4] = {1.0f, 1.0f, 1.0f, 0.0f};


        JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoInitialise(JNIEnv * env, jobject object)) {

            const char *model0file = "/storage/emulated/0/L_CATALOGUE/cache/Data/models/bedsofa.obj";
            const char *model1file = "/storage/emulated/0/L_CATALOGUE/cache/Data/models/dressing_table.obj";
            const char *model2file = "/storage/emulated/0/L_CATALOGUE/cache/Data/models/outdoorsofa.obj";
            const char *model3file = "/storage/emulated/0/L_CATALOGUE/cache/Data/models/wardrobe.obj";
            const char *model4file = "/storage/emulated/0/L_CATALOGUE/cache/Data/models/study_table.obj";
            const char *model5file = "/storage/emulated/0/L_CATALOGUE/cache/Data/models/parasona.obj";
            const char *model6file = "/storage/emulated/0/L_CATALOGUE/cache/Data/models/dinning.obj";
            const char *model7file = "/storage/emulated/0/L_CATALOGUE/cache/Data/models/TEAKBED.obj";
            const char *model8file = "/storage/emulated/0/L_CATALOGUE/cache/Data/models/wallpaint.obj";
            const char *model9file = "/storage/emulated/0/L_CATALOGUE/cache/Data/models/folorencecompactsofa.obj";

            //Mapping to pattern 0 - bed sofa.obj
            models[0].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOGUE/cache/Data/patterns/one.patt;80");

            arwSetMarkerOptionBool(models[0].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[0].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[0].obj = glmReadOBJ2(model0file, 0, 0); // context 0, don't read textures yet.
                if (!models[0].obj) {
                    LOGE("Error loading model from file '%s'.", model0file);
                    exit(-1);
                }
            glmScale(models[0].obj, 15.0f);
                //glmRotate(models[0].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[0].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE);
            models[0].visible = false;

            //Mapping to pattern 1 - dressing_table.obj
            models[1].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOGUE/cache/Data/patterns/two.patt;80");
            arwSetMarkerOptionBool(models[1].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[1].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[1].obj = glmReadOBJ2(model1file, 0, 0); // context 1, don't read textures yet.
                if (!models[1].obj) {
                    LOGE("Error loading model from file '%s'.", model1file);
                    exit(-1);
                }
            glmScale(models[1].obj, 15.0f);
                //glmRotate(models[1].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[1].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE);
            models[1].visible = false;

            //Mapping to pattern 2 - outdoor sofa.obj
            models[2].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOGUE/cache/Data/patterns/three.patt;80");
            arwSetMarkerOptionBool(models[2].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[2].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[2].obj = glmReadOBJ2(model2file, 0, 0); // context 2, don't read textures yet.
                    if (!models[2].obj) {
                      LOGE("Error loading model from file '%s'.", model2file);
                      exit(-1);
                    }
            glmScale(models[2].obj, 15.0f);
                //glmRotate(models[2].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[2].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
            models[2].visible = false;

            //Mapping to pattern 3 - wardrobe.obj
            models[3].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOGUE/cache/Data/patterns/four.patt;80");
            arwSetMarkerOptionBool(models[3].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[3].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[3].obj = glmReadOBJ2(model3file, 0, 0); // context 3, don't read textures yet.
                    if (!models[3].obj) {
                      LOGE("Error loading model from file '%s'.", model3file);
                      exit(-1);
                    }
            glmScale(models[3].obj, 15.0f);
                //glmRotate(models[3].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[3].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
            models[3].visible = false;

             //Mapping to pattern 4 - study_table.obj
            models[4].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOGUE/cache/Data/patterns/five.patt;80");
            arwSetMarkerOptionBool(models[4].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[4].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[4].obj = glmReadOBJ2(model4file, 0, 0); // context 4, don't read textures yet.
                    if (!models[4].obj) {
                      LOGE("Error loading model from file '%s'.", model4file);
                      exit(-1);
                    }
            glmScale(models[4].obj, 15.0f);
                //glmRotate(models[4].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[4].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                        models[4].visible = false;

            //Mapping to pattern 5 - parasona.obj
            models[5].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOGUE/cache/Data/patterns/six.patt;80");
            arwSetMarkerOptionBool(models[5].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[5].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[5].obj = glmReadOBJ2(model5file, 0, 0); // context 5, don't read textures yet.
                   if (!models[5].obj) {
                     LOGE("Error loading model from file '%s'.", model5file);
                     exit(-1);
                   }
            glmScale(models[5].obj, 15.0f);
               //glmRotate(models[5].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[5].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                       models[5].visible = false;

            //Mapping to pattern 6 - dinning.obj
            models[6].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOGUE/cache/Data/patterns/seven.patt;80");
            arwSetMarkerOptionBool(models[6].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[6].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[6].obj = glmReadOBJ2(model6file, 0, 0); // context 6, don't read textures yet.
                  if (!models[6].obj) {
                    LOGE("Error loading model from file '%s'.", model6file);
                    exit(-1);
                  }
            glmScale(models[6].obj, 15.0f);
              //glmRotate(models[6].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[6].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                      models[6].visible = false;

            //Mapping to pattern 7 - teak bed.obj
            models[7].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOGUE/cache/Data/patterns/eight.patt;80");
            arwSetMarkerOptionBool(models[7].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[7].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[7].obj = glmReadOBJ2(model7file, 0, 0); // context 7, don't read textures yet.
                if (!models[7].obj) {
                  LOGE("Error loading model from file '%s'.", model7file);
                  exit(-1);
                }
            glmScale(models[7].obj, 15.0f);
            //glmRotate(models[7].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[7].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[7].visible = false;

            //Mapping to pattern 8 - wall paint.obj
            models[8].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOGUE/cache/Data/patterns/A_4x4.patt;80");
            arwSetMarkerOptionBool(models[8].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[8].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[8].obj = glmReadOBJ2(model8file, 0, 0); // context 8, don't read textures yet.
                if (!models[8].obj) {
                  LOGE("Error loading model from file '%s'.", model8file);
                  exit(-1);
                }
            glmScale(models[8].obj, 15.0f);
            //glmRotate(models[8].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[8].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[8].visible = false;

            //Mapping to pattern 9 - florence compact sofa.obj
            models[9].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOGUE/cache/Data/patterns/B_4x4.patt;80");
            arwSetMarkerOptionBool(models[9].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[9].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[9].obj = glmReadOBJ2(model9file, 0, 0); // context 9, don't read textures yet.
                if (!models[9].obj) {
                  LOGE("Error loading model from file '%s'.", model9file);
                  exit(-1);
                }
            glmScale(models[9].obj, 5.0f);
            //glmRotate(models[9].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[9].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[9].visible = false;
        }

    JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoShutdown(JNIEnv * env, jobject object)) {}

    JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceCreated(JNIEnv * env, jobject object)) {
         glStateCacheFlush(); // Make sure we don't hold outdated OpenGL state.
         for (int i = 0;i < NUM_MODELS; i++) {
             if (models[i].obj) {
                 glmDelete(models[i].obj, 0);
                 models[i].obj = NULL;
             }
         }
     }

    JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceChanged(JNIEnv * env, jobject object, jint w, jint h)) {
        // glViewport(0, 0, w, h) has already been set.
    }

    JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoDrawFrame(JNIEnv * env, jobject obj)) {

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Set the projection matrix to that provided by ARToolKit.
        float proj[16];
        arwGetProjectionMatrix(proj);
        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(proj);
        glMatrixMode(GL_MODELVIEW);

        glStateCacheEnableDepthTest();

        glStateCacheEnableLighting();

        glEnable(GL_LIGHT0);

        for (int i = 0;i < NUM_MODELS; i++) {
            models[i].visible = arwQueryMarkerTransformation(models[i].patternID, models[i].transformationMatrix);

            if (models[i].visible) {
                glLoadMatrixf(models[i].transformationMatrix);
                glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
                glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
                glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);
                glmDrawArrays(models[i].obj, 0);
            }
        }
    }
