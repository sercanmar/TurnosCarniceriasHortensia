@echo off
echo arrancando contenedores de mysql y odoo...
docker-compose up -d

echo.
echo bases de datos y entorno odoo levantados correctamente.
echo ya puedes darle a 'Run' a tu clase Main.java en tu editor.
echo.
pause