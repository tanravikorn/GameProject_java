package application;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.utils.MatchFinder;
import logic.utils.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // 1. สร้างกระดาน 5x5
        System.out.println("--- 1. Init Board ---");
        Board board = new Board(5, 5);

        // เติมลูกกวาดมั่วๆ ไปก่อน (สี BLUE)
        // วิธีถมดำแบบ "ตารางหมากรุก" (Chessboard Pattern)
// สีจะสลับกันไปเรื่อยๆ รับรองไม่มี Match แน่นอน
        CandyColor[] bgColors = {CandyColor.GREEN, CandyColor.YELLOW}; // ใช้เขียวสลับเหลือง

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                // สูตรสลับสี: (แถว+หลัก) หาร 2 เอาเศษ
                CandyColor color = bgColors[(r + c) % 2];
                board.setCandy(r, c, new Candy(r, c, color));
            }
        }

        // 2. สร้างสถานการณ์: เรียง 3 สีแดงแนวนอน (แถว 0)
        System.out.println("--- 2. Setup Scenario: Red Match at Row 0 ---");
        Candy c1 = new Candy(0, 0, CandyColor.RED);
        Candy c2 = new Candy(0, 1, CandyColor.RED);
        Candy c3 = new Candy(0, 2, CandyColor.RED);

        // *ลองเปลี่ยน c2 เป็นระเบิดลายทางดู* (เพื่อเทส Polymorphism)
        c2.setType(CandyType.STRIPED_VER); // ให้ตัวกลางเป็นระเบิดแนวตั้ง
        board.setCandy(0, 0, c1);
        board.setCandy(0, 1, c2);
        board.setCandy(0, 2, c3);

        // 3. สั่ง MatchFinder ทำงาน
        System.out.println("--- 3. Find Matches ---");
        List<Set<Candy>> matches = MatchFinder.findAllMatches(board);
        System.out.println("Found " + matches.size() + " matched candies.");

        for (Candy c : matches) {
            System.out.println(" - Match at: " + c.getRow() + "," + c.getColumn() + " (" + c.getType() + ")");
        }

        // 4. สั่งระเบิด! (Chain Reaction)
        System.out.println("--- 4. Execute Explosion ---");
        Set<Point> deathNote = new HashSet<>();

        for (Candy c : matches) {
            // นี่คือ Polymorphism: ถ้าเป็น Normal ก็ลบ 1, ถ้าเป็น Striped ก็ลบทั้งแถว
            c.performExplosion(board, deathNote);
        }

        // 5. ดูผลลัพธ์ (ใครตายบ้าง)
        System.out.println("--- 5. Death Note (Candies to remove) ---");
        System.out.println("Total removed: " + deathNote.size());
        for (Point p : deathNote) {
            System.out.println(" - Remove at: " + p.r + "," + p.c);
        }
    }
}