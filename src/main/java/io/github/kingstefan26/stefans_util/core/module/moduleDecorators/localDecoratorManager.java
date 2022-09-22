package io.github.kingstefan26.stefans_util.core.module.moduleDecorators;

import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.visibleDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;

import java.util.ArrayList;
import java.util.List;

public class localDecoratorManager {
    public List<decoratorInterface> decoratorArrayList = new ArrayList<>();
    BasicModule parent;

    public localDecoratorManager(BasicModule parent, decoratorInterface... decorators) {
        this.parent = parent;
        for (decoratorInterface I : decorators) {
            if (I instanceof keyBindDecorator) keyBindDecorator = (keyBindDecorator) I;
            if (I instanceof onoffMessageDecorator)
                onoffMessageDecorator = (io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.onoffMessageDecorator) I;
            if (I instanceof presistanceDecorator)
                presistanceDecorator = (io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator) I;
            if (I instanceof visibleDecorator)
                visibleDecorator = (io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.visibleDecorator) I;
            decoratorArrayList.add(I);
        }
    }

    public io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator keyBindDecorator;
    public io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.onoffMessageDecorator onoffMessageDecorator;
    public io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator presistanceDecorator;
    public io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.visibleDecorator visibleDecorator;

}
