package com.pony.ninjarpc.worker;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Emulator;
import com.github.unidbg.Module;
import com.github.unidbg.file.FileResult;
import com.github.unidbg.file.IOResolver;
import com.github.unidbg.file.linux.AndroidFileIO;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

public class TTEncryptWorker extends AbstractJni implements IOResolver<AndroidFileIO> {

    private final AndroidEmulator emulator;
    private final VM vm;
    private final Module module;
    private final DvmClass TTEncryptUtils;

    public TTEncryptWorker() {
        emulator = AndroidEmulatorBuilder.for32Bit()
                .setProcessName("com.ss.android.ugc.aweme")
                .build();
        Memory memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23));
        vm = emulator.createDalvikVM(new File("./dy20.8.apk"));
        emulator.getSyscallHandler().addIOResolver(this);
        vm.setJni(this);
//        vm.setVerbose(true);

        DalvikModule dm = vm.loadLibrary(new File("./libEncryptor.so"), false);
        module = dm.getModule();
        dm.callJNI_OnLoad(emulator);
        TTEncryptUtils = vm.resolveClass("com/bytedance/frameworks/encryptor/EncryptorUtil");

    }

    public static void main(String[] args) throws Exception {
        TTEncryptWorker TTEncryptWorker = new TTEncryptWorker();
        TTEncryptWorker.encrypt("{\"magic_tag\":\"ss_app_log\",\"header\":{\"display_name\":\"抖音\",\"update_version_code\":25209900,\"manifest_version_code\":250201,\"app_version_minor\":\"\",\"aid\":1128,\"channel\":\"shenmasem_ls_dy_293\",\"package\":\"com.ss.android.ugc.aweme\",\"app_version\":\"25.2.0\",\"version_code\":250200,\"sdk_version\":\"3.7.3-rc.9-douyin\",\"sdk_target_version\":29,\"git_hash\":\"f25613a\",\"os\":\"Android\",\"os_version\":\"9\",\"os_api\":28,\"device_model\":\"G011C\",\"device_brand\":\"google\",\"device_manufacturer\":\"google\",\"device_category\":\"phone\",\"cpu_abi\":\"armeabi-v7a\",\"release_build\":\"404b3cf_20230424_147ec4de-e29d-11ed-ba56-0242ac110002\",\"density_dpi\":300,\"display_density\":\"mdpi\",\"resolution\":\"1600x1000\",\"language\":\"zh\",\"timezone\":8,\"access\":\"wifi\",\"not_request_sender\":0,\"carrier\":\"China Mobile GSM\",\"mcc_mnc\":\"46000\",\"rom\":\"rel.cjw.20220518.114133\",\"rom_version\":\"google-user 9.0.0 20171130.276299 release-keys\",\"sig_hash\":\"aea615ab910015038f73c47e45d21466\",\"openudid\":\"55b9fbd1695e6159\",\"clientudid\":\"a4463ef8-35f4-47e0-8c33-45e7f3bbee69\",\"region\":\"CN\",\"tz_name\":\"Asia\\/Shanghai\",\"tz_offset\":28800,\"sim_region\":\"cn\",\"mc\":\"8C:66:3E:0C:BF:B6\",\"udid\":\"867535167670774\",\"udid_list\":[{},{},{\"id\":\"867535167670774\",\"slot_index\":0,\"type\":\"imei\"},{\"id\":\"867535167670774\",\"slot_index\":1,\"type\":\"imei\"}],\"serial_number\":\"16761514\",\"sim_serial_number\":[{\"sim_serial_number\":\"89014103211118510720\"}],\"build_serial\":\"16761514\",\"oaid_may_support\":false,\"req_id\":\"8527c004-f871-41bf-8a40-83e058cb7b85\",\"device_platform\":\"android\",\"custom\":{\"app_session_id_vcloud\":\"Mjc4MzM3ODMxNjgzMDI4MDc0MzAy\",\"is_new_user\":true,\"is_64_apk\":false},\"apk_first_install_time\":1683027825318,\"is_system_app\":0,\"sdk_flavor\":\"china\",\"guest_mode\":0},\"_gen_time\":1683028106996}");
        TTEncryptWorker.destroy();
    }

    public String encrypt(String data) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8192);
        GZIPOutputStream gZIPOutputStream = null;
        try {
            gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(data.getBytes());
            gZIPOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        ByteArray array = TTEncryptUtils.callStaticJniMethodObject(emulator, "ttEncrypt([BI)[B",
                new ByteArray(vm, byteArray), byteArray.length); // 执行Jni方法
        byte[] encode = Base64.getEncoder().encode(array.getValue());
        System.out.println(new String(encode));
        return new String(encode);
    }

    public void destroy() throws IOException {
        emulator.close();
    }

    @Override
    public DvmObject<?> callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "java/nio/charset/Charset->defaultCharset()Ljava/nio/charset/Charset;":
                DvmObject<?> dvmObject = vm.resolveClass("java/nio/charset/Charset")
                        .newObject(Charset.defaultCharset());
                return dvmObject;
        }
        return super.callStaticObjectMethodV(vm, dvmClass, signature, vaList);
    }


    @Override
    public FileResult<AndroidFileIO> resolve(Emulator<AndroidFileIO> emulator, String pathname, int oflags) {
        System.out.println(pathname);
        return null;
    }
}
