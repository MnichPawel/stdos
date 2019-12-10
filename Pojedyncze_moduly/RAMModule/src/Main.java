public class Main {
    public static void main(String[] args){
        RAMModule module = new RAMModule();
        module.wypisz_podzial();

        System.out.println("----------");

        int rezerwacja_1 = module.zarezerwuj_pamiec(1);
        module.wypisz_podzial();

        System.out.println("----------");

        int rezerwacja_2 = module.zarezerwuj_pamiec(1);
        module.wypisz_podzial();

        System.out.println("----------");

        int rezerwacja_3 = module.zarezerwuj_pamiec(9);
        module.wypisz_podzial();

        System.out.println("----------");

        int rezerwacja_4 = module.zarezerwuj_pamiec(1);
        module.wypisz_podzial();

        System.out.println("----------");

        int rezerwacja_5 = module.zarezerwuj_pamiec(1);
        module.wypisz_podzial();

        System.out.println("----------");

        int rezerwacja_6 = module.zarezerwuj_pamiec(5);
        module.wypisz_podzial();

        System.out.println("----------");

        int rezerwacja_7 = module.zarezerwuj_pamiec(1);
        module.wypisz_podzial();

        System.out.println("----------");

        int rezerwacja_8 = module.zarezerwuj_pamiec(1);
        module.wypisz_podzial();

        System.out.println("----------");

        module.zwolnij_pamiec(rezerwacja_4);
        module.wypisz_podzial();

        System.out.println("----------");

        module.zwolnij_pamiec(rezerwacja_1);
        module.wypisz_podzial();

        System.out.println("----------");

        module.zwolnij_pamiec(rezerwacja_2);
        module.wypisz_podzial();

        System.out.println("----------");

        module.zwolnij_pamiec(rezerwacja_5);
        module.wypisz_podzial();

        System.out.println("----------");

        module.wypisz_pamiec();

        module.zapisz_bajt( (byte)255, rezerwacja_6);

        System.out.println("----------");

        module.wypisz_pamiec();

        byte[] res = new byte[]{ (byte)54, (byte)114, (byte)32, (byte)12, (byte)99 };

        module.zapisz_bajty(res, rezerwacja_6);

        System.out.println("----------");

        module.wypisz_pamiec();

        byte[] odczyt = module.odczytaj_bajty(rezerwacja_6, 5);

        for(int i = 0; i < odczyt.length; ++i){
            System.out.println(odczyt[i]);
        }

    }
}
