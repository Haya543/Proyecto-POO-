#!/usr/bin/env bash
# Builds the Java backend and starts both servers.
# Run from the project root: ./run.sh

set -e

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo ""
echo "=== Compilando backend Java ==="
cd "$ROOT_DIR"
mvn -q package -DskipTests
echo "Backend compilado correctamente."

echo ""
echo "=== Instalando dependencias del frontend ==="
cd "$ROOT_DIR/ui"
npm install --silent
echo "Dependencias listas."

echo ""
echo "=== Iniciando servidor Java en puerto 7070 ==="
cd "$ROOT_DIR"
java -jar target/juego-1.0-jar-with-dependencies.jar &
JAVA_PID=$!

# Give the Java server a moment to start before launching the frontend
sleep 2

echo ""
echo "=== Iniciando frontend React en puerto 5173 ==="
cd "$ROOT_DIR/ui"
npm run dev &
REACT_PID=$!

echo ""
echo "================================================"
echo "  Juego disponible en: http://localhost:5173"
echo "  API backend en:      http://localhost:7070"
echo "================================================"
echo ""
echo "Presiona Ctrl+C para detener ambos servidores."

# On Ctrl+C, kill both servers cleanly
trap "echo ''; echo 'Deteniendo servidores...'; kill $JAVA_PID $REACT_PID 2>/dev/null; exit 0" INT TERM

wait
