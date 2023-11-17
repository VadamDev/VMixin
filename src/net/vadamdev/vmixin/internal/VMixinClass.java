package net.vadamdev.vmixin.internal;

import javassist.*;
import net.vadamdev.vmixin.api.VMixin;
import net.vadamdev.vmixin.api.injection.Inject;
import net.vadamdev.vmixin.internal.utils.Utils;

import java.lang.reflect.Method;

/**
 * @author VadamDev
 * @since 17/11/2023
 */
public class VMixinClass {
    private final CtClass mixinClass;
    private final Class<?> jMixinClass;

    public VMixinClass(String mixinClass) throws NotFoundException, ClassNotFoundException {
        this.mixinClass = Utils.CLASS_POOL.get(mixinClass);
        this.jMixinClass = Class.forName(mixinClass);
    }

    public void inject() throws NotFoundException, CannotCompileException {
        final VMixin mixin = jMixinClass.getDeclaredAnnotation(VMixin.class);
        if(mixin == null)
            throw new NullPointerException("Unable to find vmixin annotation in the mixin class");

        final CtClass targetClass = Utils.CLASS_POOL.get(mixin.target());

        for(Method method : jMixinClass.getDeclaredMethods()) {
            final Inject injectInfo = method.getDeclaredAnnotation(Inject.class);
            if(injectInfo == null)
                continue;

            final CtMethod hook = CtNewMethod.copy(mixinClass.getDeclaredMethod(method.getName()), targetClass, null);
            hook.setName("__hook_" + method.getName());
            targetClass.addMethod(hook);

            final CtMethod toHook = targetClass.getDeclaredMethod(injectInfo.method());

            String src = "__hook_" + method.getName() + "();";
            switch(injectInfo.at()) {
                case TOP:
                    toHook.insertBefore(src);
                    break;
                case BOTTOM:
                    toHook.insertAfter(src);
                    break;
                default:
                    break;
            }
        }

        targetClass.toClass();
    }
}
