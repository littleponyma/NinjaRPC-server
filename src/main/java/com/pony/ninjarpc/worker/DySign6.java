package com.pony.ninjarpc.worker;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Emulator;
import com.github.unidbg.Module;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.file.FileResult;
import com.github.unidbg.file.IOResolver;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.array.ArrayObject;
import com.github.unidbg.linux.android.dvm.wrapper.DvmBoolean;
import com.github.unidbg.linux.file.SimpleFileIO;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.pointer.UnidbgPointer;

import java.io.File;
import java.util.HashMap;

public class DySign6 extends AbstractJni implements IOResolver {

    private final AndroidEmulator emulator;
    private final VM vm;
    private final Module module;
    private final Memory memory;

    public DySign6() {
        emulator = AndroidEmulatorBuilder
                .for32Bit()
                .setProcessName("com.ss.android.ugc.aweme")
                .addBackendFactory(new Unicorn2Factory(true))
                .build();
        emulator.getBackend().registerEmuCountHook(100000);
        emulator.getSyscallHandler().setVerbose(true);
        emulator.getSyscallHandler().setEnableThreadDispatcher(true);
        emulator.getSyscallHandler().addIOResolver(this);

        memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23));

        vm = emulator.createDalvikVM();
        vm.setJni(this);
//        vm.setVerbose(true);

        vm.resolveClass("com/bytedance/mobsec/metasec/ml/MS",
                vm.resolveClass("ms/bd/c/a0",
                        vm.resolveClass("ms/bd/c/k")));

        DalvikModule dm = vm.loadLibrary(new File("./libmetasec_ml.so"), true);
        module = dm.getModule();
        module.findSymbolByName("JNI_OnLoad", false).call(emulator, vm.getJavaVM(), null);
    }

    @Override
    public DvmObject<?> callObjectMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        System.out.println("callObjectMethodV "+ signature);
        switch (signature) {
            case "java/lang/Thread->getStackTrace()[Ljava/lang/StackTraceElement;": {
                DvmObject<?>[] a = {
                        vm.resolveClass("java/lang/StackTraceElement").newObject("dalvik.system.VMStack"),
                        vm.resolveClass("java/lang/StackTraceElement").newObject("java.lang.Thread")
                };
                return new ArrayObject(a);
            }
        }
        return super.callObjectMethodV(vm, dvmObject, signature, vaList);
    }

    @Override
    public DvmObject<?> callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        System.out.println("callStaticObjectMethodV "+ signature);
        switch (signature) {
            case "com/bytedance/mobsec/metasec/ml/MS->b(IIJLjava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;": {
                int a = vaList.getIntArg(0);
                System.out.println("----------------------------");
                System.out.println(a);
                System.out.println("----------------------------");
                if (a == 65539) {
                    return new StringObject(vm,"/data/user/0/com.ss.android.ugc.aweme/files/;o@Y0f");
                } else if (a == 33554433) {
                    return DvmBoolean.valueOf(vm, Boolean.TRUE);
                } else if (a == 33554434) {
                    return DvmBoolean.valueOf(vm, Boolean.TRUE);
                } else if (a == 16777233) {
                    return new StringObject(vm, "23.4.0");
                }
            }
            case "java/lang/Thread->currentThread()Ljava/lang/Thread;": {
                return vm.resolveClass("java/lang/Thread").newObject(Thread.currentThread());
            }
        }
        return super.callStaticObjectMethodV(vm, dvmClass, signature, vaList);
    }

    @Override
    public void callStaticVoidMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        System.out.println("callStaticVoidMethodV "+ signature);
        switch (signature) {
            case "com/bytedance/mobsec/metasec/ml/MS->a()V": {
                return;
            }
        }
        super.callStaticVoidMethodV(vm, dvmClass, signature, vaList);
    }

    public HashMap<String, String> getSign(String url, String header) {
        Number ret = module.callFunction(emulator,
                0x438c0+1, url, header
        );
        UnidbgPointer p = memory.pointer(ret.intValue());
        String string = p.getString(0);
        String[] split = string.split("\r\n");
        HashMap<String,String> map=new HashMap<>();
        for (int i = 0; i < split.length; i+=2) {
            map.put(split[i],split[i+1]);
        }
        return map;
    }

    public static void main(String[] args) {
        String s1 = "https://aweme.snssdk.com/aweme/v1/aweme/post/?source=0&publish_video_strategy_type=2&max_cursor=0&sec_user_id=MS4wLjABAAAAwgMD7Og-2iZSsPa6VAext-WC1FAfInr1sK0h1Wjz4tYmBI4dtRHTJM_w38kD_T7N&count=20&os_api=25&device_type=SHARK%20KLE-A0&ssmix=a&manifest_version_code=110501&dpi=280&uuid=352746025814544&app_name=aweme&version_name=11.5.0&ts=1657615807&cpu_support64=true&app_type=normal&ac=wifi&host_abi=armeabi-v7a&channel=gdt_growth14_big_yybwz&update_version_code=11509900&_rticket=1657615810224&device_platform=android&iid=356707718149048&version_code=110500&mac_address=02%3A00%3A00%3A00%3A00%3A00&cdid=6525872d-9292-4f6b-94d3-01545bce2527&openudid=8d56e89186496918&device_id=3523301206394328&resolution=1080*1920&device_brand=blackshark&language=zh&os_version=7.1.2&aid=1128&mcc_mnc=46000";
        String s2 = "x-ss-req-ticket\r\n"+
                "1657615810214\r\n"+
                "sdk-version\r\n"+
                "2\r\n"+
                "user-agent\r\n"+
                "okhttp/3.10.0.1";
        DySign6 sign6 = new DySign6();
        HashMap<String,String> sign = sign6.getSign(s1, s2);
        System.out.println(sign);
    }

    @Override
    public FileResult resolve(Emulator emulator, String pathname, int oflags) {
        if(pathname.equals("/proc/self/exe")){
            return FileResult.success(new SimpleFileIO(oflags, new File("./exe"), pathname));
        }
        return null;
    }
}