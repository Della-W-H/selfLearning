package com.hong.SomeThingSimpleButDegraded;

/**
 * @author wanghong
 * @date 2022/7/27
 * @apiNote 内部类 如何 调用外部类的方法
 */
public class twelve_innerClassHowtouseOutClassMethdod {
    public static void main(String[] args) {
        outClassA outClassA = new outClassA();
        Speak speak = outClassA.new InnerClassB();

        speak.innerSpeak2();
    }
}

class outClassA{

    private void outSpeak(){
        System.out.println("outSpeak be using....");
    }

    class InnerClassB implements Speak{
        private void innerSpeak(){
            outClassA.this.outSpeak();
        }

        @Override
        public void innerSpeak2(){
            //间接的间接
            this.innerSpeak();
            //直接
            outSpeak();
            //通过类名 间接
            outClassA.this.outSpeak();
        }
    }
}

interface Speak{
    void innerSpeak2();
}